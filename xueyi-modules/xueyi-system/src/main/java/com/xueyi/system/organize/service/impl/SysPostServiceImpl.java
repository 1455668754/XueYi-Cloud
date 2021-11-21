package com.xueyi.system.organize.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.AuthorityConstants;
import com.xueyi.common.core.constant.BaseConstants;
import com.xueyi.common.core.exception.ServiceException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.utils.multiTenancy.TreeUtils;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.api.domain.organize.SysPost;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.organize.domain.deptPostVo;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.organize.service.ISysPostService;
import com.xueyi.system.role.domain.SysOrganizeRole;
import com.xueyi.system.role.mapper.SysOrganizeRoleMapper;
import com.xueyi.system.utils.vo.TreeSelect;
import org.apache.commons.collections4.list.TreeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 岗位信息 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#isolate")
public class SysPostServiceImpl implements ISysPostService {

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysOrganizeRoleMapper organizeRoleMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return postMapper.selectPostList(post);
    }

    /**
     * 通过岗位Id查询岗位信息
     *
     * @param post 岗位信息 | postId 岗位Id
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(SysPost post) {
        return postMapper.selectPostById(post);
    }

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    @Transactional
    @DataScope(ueAlias = "empty")
    public int insertPost(SysPost post) {
        // 欲启用岗位时判断归属部门是否启用，未启用则设置本岗位为禁用状态
        if (StringUtils.equals(BaseConstants.Status.NORMAL.getCode(), post.getStatus())) {
            SysDept dept = new SysDept(post.getDeptId());
            SysDept info = deptMapper.selectDeptById(dept);
            if (StringUtils.isNotNull(info) && StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), info.getStatus())) {
                post.setStatus(BaseConstants.Status.DISABLE.getCode());
                try {
                    throw new ServiceException(String.format("%1$s归属部门已停用,无法启用该岗位", post.getPostName()));
                } catch (Exception ignored) {
                }
            }
        }
        int row = postMapper.insertPost(post);
        if (row > 0) {
            SysRole role = new SysRole();
            role.setType(AuthorityConstants.RoleType.DERIVE_POST.getCode());
            role.setDeriveId(post.getSnowflakeId());
            role.setName("岗位衍生:" + post.getSnowflakeId());
            roleService.insertRole(role);
        }
        return row;
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePost(SysPost post) {
        // 欲启用岗位时判断归属部门是否启用，未启用则设置本岗位为禁用状态
        if (StringUtils.equals(BaseConstants.Status.NORMAL.getCode(), post.getStatus())) {
            SysDept dept = new SysDept(post.getDeptId());
            SysDept info = deptMapper.selectDeptById(dept);
            if (StringUtils.isNotNull(info) && StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), info.getStatus())) {
                post.setStatus(BaseConstants.Status.DISABLE.getCode());
                try {
                    throw new ServiceException(String.format("%1$s归属部门已停用,无法启用该岗位", post.getPostName()));
                } catch (Exception ignored) {
                }
                updatePostStatus(post);//修改保存岗位状态
            }
            SysPost check = new SysPost(post.getPostId());
            SysPost oldPost = postMapper.selectPostById(check);
            if (StringUtils.isNotNull(info) && !Objects.equals(oldPost.getDeptId(), post.getDeptId())) {
                SysUser user = new SysUser();
                user.setPostId(post.getPostId());
                user.setDeptId(post.getDeptId());
                userMapper.updateUserDeptByPostId(user);
            }
        }
        // 执行岗位状态变更
        return postMapper.updatePost(post);
    }

    /**
     * 修改保存岗位-角色信息
     *
     * @param post 岗位信息 | postId  岗位Id | roleIds 角色组Ids
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePostRole(SysPost post) {
        // 1.删除原有岗位-角色关联
        SysOrganizeRole organizeRole = new SysOrganizeRole();
        organizeRole.setPostId(post.getPostId());
        organizeRoleMapper.deleteOrganizeRoleByOrganizeId(organizeRole);
        // 2.是否需要执行新增
        if (post.getRoleIds().length > 0) {
            organizeRole.getParams().put("roleIds", post.getRoleIds());
            organizeRoleMapper.batchOrganizeRole(organizeRole);
        }
        return 1;
    }

    /**
     * 修改保存岗位状态
     *
     * @param post 岗位信息 | postId 岗位Id | status 部门状态
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePostStatus(SysPost post) {
        //操作逻辑：当欲设置禁用时，同步执行禁用本岗位所属用户
        int rows;
        SysUser user = new SysUser();
        user.setPostId(post.getPostId());
        user.setStatus(post.getStatus());
        // 变更岗位状态
        rows = postMapper.updatePostStatus(post);
        // 欲停用时停用本岗位所有用户的状态
        if (rows > 0 && StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), post.getStatus())) {
            rows = rows + userMapper.updateUserStatusByPostId(user);
        }
        return rows;
    }

    /**
     * 删除岗位信息
     *
     * @param post 岗位信息 | postId 岗位Id
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePostById(SysPost post) {
        // 1.删除衍生role信息
        SysRole role = new SysRole();
        role.setType(AuthorityConstants.RoleType.DERIVE_POST.getCode());
        role.setDeriveId(post.getPostId());
        roleMapper.deleteRoleByDeriveId(role);
        // 2.删除岗位-角色关联信息
        SysOrganizeRole organizeRole = new SysOrganizeRole();
        organizeRole.setPostId(post.getPostId());
        organizeRole.setDerivePostId(post.getPostId());
        organizeRoleMapper.deleteOrganizeRoleByOrganizeId(organizeRole);
        return postMapper.deletePostById(post);
    }

    /**
     * 批量删除岗位信息
     *
     * @param post 岗位信息 | params.Ids 需要删除的岗位Ids组
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePostByIds(SysPost post) {
        // 1.批量删除衍生role信息
        SysRole role = new SysRole();
        role.setType(AuthorityConstants.RoleType.DERIVE_POST.getCode());
        role.getParams().put("Ids", post.getParams().get("Ids"));
        roleMapper.deleteRoleByDeriveIds(role);
        // 2.批量删除岗位-角色关联信息
        SysOrganizeRole organizeRole = new SysOrganizeRole();
        organizeRole.setPostId(AuthorityConstants.DELETE_PARAM);
        organizeRole.setDerivePostId(AuthorityConstants.DELETE_PARAM);
        organizeRole.getParams().put("Ids", post.getParams().get("Ids"));
        organizeRoleMapper.deleteOrganizeRoleByOrganizeIds(organizeRole);
        return postMapper.deletePostByIds(post);
    }

    /**
     * 校验岗位是否存在用户
     *
     * @param post 岗位信息 | postId 岗位Id
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkPostExistUser(SysPost post) {
        SysUser user = new SysUser();
        user.setPostId(post.getPostId());
        int result = userMapper.checkPostExistUser(user);
        return result > 0;
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息 | postId   岗位Id | postCode 岗位编码
     * @return 结果
     */
    @Override
    public String checkPostCodeUnique(SysPost post) {
        if (StringUtils.isNull(post.getPostId())) {
            post.setPostId(-1L);
        }
        SysPost info = postMapper.checkPostCodeUnique(post);
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != post.getPostId().longValue()) {
            return BaseConstants.Check.NOT_UNIQUE.getCode();
        }
        return BaseConstants.Check.UNIQUE.getCode();
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息 | postId   岗位Id | deptId   部门Id | postName 岗位名称
     * @return 结果
     */
    @Override
    public String checkPostNameUnique(SysPost post) {
        if (StringUtils.isNull(post.getPostId())) {
            post.setPostId(-1L);
        }
        SysPost info = postMapper.checkPostNameUnique(post);
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != post.getPostId().longValue()) {
            return BaseConstants.Check.NOT_UNIQUE.getCode();
        }
        return BaseConstants.Check.UNIQUE.getCode();
    }

    /**
     * 校验岗位状态
     *
     * @param post 岗位信息 | postId 岗位Id
     * @return 结果
     */
    @Override
    public String checkPostStatus(SysPost post) {
        if (StringUtils.isNotNull(post.getPostId())) {
            SysPost info = postMapper.selectPostById(post);
            if (StringUtils.isNotNull(info) && StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), info.getStatus())) {
                return BaseConstants.Status.DISABLE.getCode();
            }
        }
        return BaseConstants.Status.NORMAL.getCode();
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptPostTreeSelect() {
        List<SysDept> deptList = deptMapper.selectDeptList(new SysDept());
        List<SysPost> postList = postMapper.selectPostList(new SysPost());
        List<deptPostVo> deptPostList = new TreeList<>();
        deptPostVo deptPost;
        for (SysDept dept : deptList) {
            deptPost = new deptPostVo(dept.getDeptId(), dept.getParentId(), "部门|" + dept.getDeptName(), dept.getStatus(), "0");
            deptPostList.add(deptPost);
        }
        for (SysPost post : postList) {
            deptPost = new deptPostVo(post.getPostId(), post.getDeptId(), "岗位|" + post.getPostName(), post.getStatus(), "1");
            deptPostList.add(deptPost);
        }
        List<deptPostVo> trees = TreeUtils.buildTree(deptPostList, "Uid", "FUid", "children", null, false);
        return trees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }
}