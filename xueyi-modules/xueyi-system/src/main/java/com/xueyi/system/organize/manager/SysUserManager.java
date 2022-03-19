package com.xueyi.system.organize.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.security.utils.SecurityUtils;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;
import com.xueyi.system.api.organize.domain.dto.SysUserDto;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.organize.domain.merge.SysOrganizeRoleMerge;
import com.xueyi.system.organize.domain.merge.SysUserPostMerge;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.organize.mapper.merge.SysOrganizeRoleMergeMapper;
import com.xueyi.system.organize.mapper.merge.SysUserPostMergeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysUserManager extends BaseManager<SysUserDto, SysUserMapper> {

    @Autowired
    SysUserPostMergeMapper userPostMergeMapper;

    @Autowired
    SysOrganizeRoleMergeMapper organizeRoleMergeMapper;

    @Autowired
    SysPostMapper postMapper;

    @Autowired
    SysDeptMapper deptMapper;

    @Autowired
    SysRoleMapper roleMapper;

    /**
     * 用户登录校验 | 查询用户信息
     *
     * @param userName 用户账号
     * @param password 用户密码
     * @return 用户对象
     */

    public SysUserDto userLogin(String userName, String password) {
        SysUserDto userDto = baseMapper.selectOne(
                Wrappers.<SysUserDto>query().lambda()
                        .eq(SysUserDto::getUserName, userName));
        //check password is true
        if (ObjectUtil.isNull(userDto) || !SecurityUtils.matchesPassword(password, userDto.getPassword()))
            return null;

        // select posts in user && select depts in post
        List<SysUserPostMerge> userPostMerges = userPostMergeMapper.selectList(
                Wrappers.<SysUserPostMerge>query().lambda()
                        .eq(SysUserPostMerge::getUserId, userDto.getId()));

        List<Long> postIds = userPostMerges.stream().map(SysUserPostMerge::getPostId).collect(Collectors.toList());
        List<Long> deptIds = null;
        // if exist posts, must exist depts
        if (CollUtil.isNotEmpty(userPostMerges)) {
            userDto.setPosts(postMapper.selectBatchIds(postIds));
            if (CollUtil.isNotEmpty(userDto.getPosts())) {
                deptIds = userDto.getPosts().stream().map(SysPostDto::getDeptId).collect(Collectors.toList());
                List<SysDeptDto> depts = deptMapper.selectBatchIds(deptIds);
                for (SysDeptDto deptDto : depts) {
                    for (int i = 0; i < userDto.getPosts().size(); i++) {
                        if (ObjectUtil.equal(userDto.getPosts().get(i).getDeptId(), deptDto.getId())) {
                            userDto.getPosts().get(i).setDept(deptDto);
                            break;
                        }
                    }
                }
            }
        }

        // select roles in user
        LambdaQueryWrapper<SysOrganizeRoleMerge> organizeRoleMergeQueryWrapper = new LambdaQueryWrapper<>();
        organizeRoleMergeQueryWrapper
                .eq(SysOrganizeRoleMerge::getUserId, userDto.getId());
        if (CollUtil.isNotEmpty(postIds))
            organizeRoleMergeQueryWrapper
                    .or().in(SysOrganizeRoleMerge::getPostId, postIds)
                    .or().in(SysOrganizeRoleMerge::getDeptId, deptIds);

        List<SysOrganizeRoleMerge> organizeRoleMerges = organizeRoleMergeMapper.selectList(organizeRoleMergeQueryWrapper);
        userDto.setRoles(CollUtil.isNotEmpty(organizeRoleMerges)
                ? roleMapper.selectBatchIds(organizeRoleMerges.stream().map(SysOrganizeRoleMerge::getRoleId).collect(Collectors.toList()))
                : new ArrayList<>());
        return userDto;
    }

    /**
     * 根据Id查询单条用户对象 | 附加数据
     *
     * @param id Id
     * @return 用户对象
     */
    public SysUserDto selectByIdExtra(Serializable id) {
        SysUserDto user = baseMapper.selectById(id);
        if (ObjectUtil.isNotNull(user)) {
            List<SysUserPostMerge> userPostMerges = userPostMergeMapper.selectList(
                    Wrappers.<SysUserPostMerge>query().lambda()
                            .eq(SysUserPostMerge::getUserId, user.getId()));
            user.setPostIds(userPostMerges.stream().map(SysUserPostMerge::getPostId).toArray(Long[]::new));
        }
        return user;
    }

    /**
     * 新增用户对象
     *
     * @param user 用户对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(SysUserDto user) {
        int row = baseMapper.insert(user);
        if (row > 0) {
            if (ArrayUtil.isNotEmpty(user.getPostIds())) {
                userPostMergeMapper.insertBatch(
                        Arrays.stream(user.getPostIds())
                                .map(postId -> new SysUserPostMerge(user.getId(), postId))
                                .collect(Collectors.toSet()));
            }
        }
        return row;
    }

    /**
     * 修改用户对象
     *
     * @param user 用户对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int update(SysUserDto user) {
        int row = baseMapper.updateById(user);
        if (row > 0) {
            // 查询原关联，判断前后是否变更 ？ 新增/移除变更 : 不操作
            List<SysUserPostMerge> userPostMerges = userPostMergeMapper.selectList(
                    Wrappers.<SysUserPostMerge>query().lambda()
                            .eq(SysUserPostMerge::getUserId, user.getId()));
            List<Long> delPostIds = userPostMerges.stream().map(SysUserPostMerge::getPostId).collect(Collectors.toList());

            if (ArrayUtil.isNotEmpty(user.getPostIds())) {
                List<Long> postIds = Arrays.asList(user.getPostIds());
                List<Long> addPostIds = CollUtil.subtractToList(postIds, delPostIds);
                delPostIds.removeAll(postIds);
                if (CollUtil.isNotEmpty(addPostIds)) {
                    userPostMergeMapper.insertBatch(
                            addPostIds.stream()
                                    .map(postId -> new SysUserPostMerge(user.getId(), postId))
                                    .collect(Collectors.toSet()));
                }
            }
            if (CollUtil.isNotEmpty(delPostIds)) {
                userPostMergeMapper.delete(
                        Wrappers.<SysUserPostMerge>update().lambda()
                                .in(SysUserPostMerge::getPostId, delPostIds)
                                .eq(SysUserPostMerge::getUserId, user.getId()));
            }
        }
        return row;
    }

    /**
     * 修改用户基本信息
     *
     * @param id       用户Id
     * @param nickName 用户昵称
     * @param sex      用户性别
     * @param profile  个人简介
     * @return 结果
     */
    public int updateUserProfile(Long id, String nickName, String sex, String profile) {
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserDto>update().lambda()
                        .set(SysUserDto::getNickName, nickName)
                        .set(SysUserDto::getSex, sex)
                        .set(SysUserDto::getProfile, profile)
                        .eq(SysUserDto::getId, id));
    }

    /**
     * 更新用户账号
     *
     * @param id       用户Id
     * @param userName 用户账号
     * @return 结果
     */
    public int updateUserName(Long id, String userName) {
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserDto>update().lambda()
                        .set(SysUserDto::getUserName, userName)
                        .eq(SysUserDto::getId, id));
    }

    /**
     * 更新用户邮箱
     *
     * @param id    用户Id
     * @param email 邮箱
     * @return 结果
     */
    public int updateEmail(Long id, String email) {
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserDto>update().lambda()
                        .set(SysUserDto::getEmail, email)
                        .eq(SysUserDto::getId, id));
    }

    /**
     * 更新用户手机号
     *
     * @param id    用户Id
     * @param phone 手机号
     * @return 结果
     */
    public int updatePhone(Long id, String phone) {
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserDto>update().lambda()
                        .set(SysUserDto::getPhone, phone)
                        .eq(SysUserDto::getId, id));
    }

    /**
     * 修改用户头像
     *
     * @param id     用户Id
     * @param avatar 头像地址
     * @return 结果
     */
    public int updateUserAvatar(Long id, String avatar) {
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserDto>update().lambda()
                        .set(SysUserDto::getAvatar, avatar)
                        .eq(SysUserDto::getId, id));
    }

    /**
     * 重置用户密码
     *
     * @param id       用户Id
     * @param password 密码
     * @return 结果
     */
    public int resetUserPassword(Long id, String password) {
        return baseMapper.update(new SysUserDto(),
                Wrappers.<SysUserDto>update().lambda()
                        .set(SysUserDto::getPassword, password)
                        .eq(SysUserDto::getId, id));
    }

    /**
     * 根据Id删除用户对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteById(Serializable id) {
        int row = baseMapper.deleteById(id);
        if (row > 0) {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>update().lambda()
                            .eq(SysOrganizeRoleMerge::getUserId, id));
            userPostMergeMapper.delete(
                    Wrappers.<SysUserPostMerge>update().lambda()
                            .eq(SysUserPostMerge::getUserId, id));
        }
        return row;
    }

    /**
     * 根据Id集合批量删除用户对象
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    @DSTransactional
    public int deleteByIds(Collection<? extends Serializable> idList) {
        int rows = baseMapper.deleteBatchIds(idList);
        if (rows > 0) {
            organizeRoleMergeMapper.delete(
                    Wrappers.<SysOrganizeRoleMerge>update().lambda()
                            .in(SysOrganizeRoleMerge::getUserId, idList));
            userPostMergeMapper.delete(
                    Wrappers.<SysUserPostMerge>update().lambda()
                            .in(SysUserPostMerge::getUserId, idList));
        }
        return rows;
    }

    /**
     * 校验用户编码是否唯一
     *
     * @param id   用户Id
     * @param code 用户编码
     * @return 用户对象
     */
    public SysUserDto checkUserCodeUnique(Long id, String code) {
        return baseMapper.selectOne(
                Wrappers.<SysUserDto>query().lambda()
                        .ne(SysUserDto::getId, id)
                        .eq(SysUserDto::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验用户账号是否唯一
     *
     * @param id       Id
     * @param userName 用户账号
     * @return 结果 | true/false 唯一/不唯一
     */
    public SysUserDto checkUserNameUnique(Serializable id, String userName) {
        return baseMapper.selectOne(
                Wrappers.<SysUserDto>query().lambda()
                        .ne(SysUserDto::getId, id)
                        .eq(SysUserDto::getUserName, userName)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param id       用户Id
     * @param userName 用户名称
     * @return 用户对象
     */
    @Override
    public SysUserDto checkNameUnique(Serializable id, String userName) {
        return baseMapper.selectOne(
                Wrappers.<SysUserDto>query().lambda()
                        .ne(SysUserDto::getId, id)
                        .eq(SysUserDto::getUserName, userName)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param id    用户Id
     * @param phone 手机号码
     * @return 用户对象
     */
    public SysUserDto checkPhoneUnique(Long id, String phone) {
        return baseMapper.selectOne(
                Wrappers.<SysUserDto>query().lambda()
                        .ne(SysUserDto::getId, id)
                        .eq(SysUserDto::getPhone, phone)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验email是否唯一
     *
     * @param id    用户Id
     * @param email email
     * @return 用户对象
     */
    public SysUserDto checkEmailUnique(Long id, String email) {
        return baseMapper.selectOne(
                Wrappers.<SysUserDto>query().lambda()
                        .ne(SysUserDto::getId, id)
                        .eq(SysUserDto::getEmail, email)
                        .last(SqlConstants.LIMIT_ONE));
    }
}
