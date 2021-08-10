package com.xueyi.system.organize.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.RoleConstants;
import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.authority.SysRole;
import com.xueyi.system.api.domain.organize.SysDept;
import com.xueyi.system.api.domain.organize.SysUser;
import com.xueyi.system.authority.mapper.SysRoleMapper;
import com.xueyi.system.authority.service.ISysRoleService;
import com.xueyi.system.organize.domain.deptPostVo;
import com.xueyi.system.organize.mapper.SysDeptMapper;
import com.xueyi.system.organize.mapper.SysPostMapper;
import com.xueyi.system.organize.mapper.SysUserMapper;
import com.xueyi.system.role.domain.SysOrganizeRole;
import com.xueyi.system.role.mapper.SysOrganizeRoleMapper;
import com.xueyi.system.utils.vo.TreeSelect;
import org.apache.commons.collections4.list.TreeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xueyi.common.core.constant.UserConstants;
import com.xueyi.common.core.exception.CustomException;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.system.api.domain.organize.SysPost;
import com.xueyi.system.organize.service.ISysPostService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 岗位信息 服务层处理
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
        return postMapper.selectPostList(post);//@param post 岗位信息
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
        if (UserConstants.POST_NORMAL.equals(post.getStatus())) {
            SysDept dept = new SysDept();
            dept.setDeptId(post.getDeptId());
            SysDept info = deptMapper.selectDeptById(dept);
            if (StringUtils.isNotNull(info) && UserConstants.DEPT_DISABLE.equals(info.getStatus())) {
                post.setStatus(UserConstants.POST_DISABLE);
                try {
                    throw new CustomException(String.format("%1$s归属部门已停用,无法启用该岗位", post.getPostName()));
                } catch (Exception ignored) {
                }
            }
        }
        int row = postMapper.insertPost(post);
        if(row>0){
            SysRole role = new SysRole();
            role.setType(RoleConstants.POST_DERIVE_TYPE);
            role.setDeriveId(post.getId());
            role.setRoleName("岗位衍生"+post.getId());
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
        if (UserConstants.POST_NORMAL.equals(post.getStatus())) {
            SysDept dept = new SysDept();
            dept.setDeptId(post.getDeptId());
            SysDept info = deptMapper.selectDeptById(dept);
            if (StringUtils.isNotNull(info) && UserConstants.DEPT_DISABLE.equals(info.getStatus())) {
                post.setStatus(UserConstants.POST_DISABLE);
                try {
                    throw new CustomException(String.format("%1$s归属部门已停用,无法启用该岗位", post.getPostName()));
                } catch (Exception ignored) {
                }
                updatePostStatus(post);//修改保存岗位状态
            }
            SysPost check = new SysPost();
            check.setPostId(post.getPostId());
            SysPost oldPost = postMapper.selectPostById(check);
            if(StringUtils.isNotNull(info) && !Objects.equals(oldPost.getDeptId(), post.getDeptId())){
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
        if(post.getRoleIds().length > 0){
            organizeRole.getParams().put("roleIds",post.getRoleIds());
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
        if (rows > 0 && UserConstants.POST_DISABLE.equals(post.getStatus())) {
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
        role.setType(RoleConstants.POST_DERIVE_TYPE);
        role.setDeriveId(post.getPostId());
        roleMapper.deleteRoleByTypeAndDeriveId(role);
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
        role.setType(RoleConstants.POST_DERIVE_TYPE);
        role.getParams().put("Ids",post.getParams().get("Ids"));
        roleMapper.deleteRoleByTypeAndDeriveIds(role);
        // 2.批量删除岗位-角色关联信息
        SysOrganizeRole organizeRole = new SysOrganizeRole();
        organizeRole.setPostId(RoleConstants.DELETE_PARAM);
        organizeRole.setDerivePostId(RoleConstants.DELETE_PARAM);
        organizeRole.getParams().put("Ids",post.getParams().get("Ids"));
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
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
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
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
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
            if (StringUtils.isNotNull(info) && UserConstants.POST_DISABLE.equals(info.getStatus())) {
                return UserConstants.POST_DISABLE;
            }
        }
        return UserConstants.POST_NORMAL;
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
            deptPost = new deptPostVo();
            deptPost.setUid(dept.getDeptId());
            deptPost.setFUid(dept.getParentId());
            deptPost.setName("部门|" + dept.getDeptName());
            deptPost.setStatus(dept.getStatus());
            deptPost.setType("0");
            deptPostList.add(deptPost);
        }
        for (SysPost post : postList) {
            deptPost = new deptPostVo();
            deptPost.setUid(post.getPostId());
            deptPost.setFUid(post.getDeptId());
            deptPost.setName("岗位|" + post.getPostName());
            deptPost.setStatus(post.getStatus());
            deptPost.setType("1");
            deptPostList.add(deptPost);
        }
        List<deptPostVo> trees = buildDeptPostTree(deptPostList);
        return trees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param deptPostList 部门-岗位数组装列表
     * @return 树结构列表
     */
    @Override
    public List<deptPostVo> buildDeptPostTree(List<deptPostVo> deptPostList) {
        List<deptPostVo> returnList = new ArrayList<deptPostVo>();
        List<Long> tempList = new ArrayList<Long>();
        for (deptPostVo deptPostVo : deptPostList) {
            tempList.add(deptPostVo.getUid());
        }
        for (Iterator<deptPostVo> iterator = deptPostList.iterator(); iterator.hasNext(); ) {
            deptPostVo deptPostVo = (deptPostVo) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(deptPostVo.getFUid())) {
                recursionFn(deptPostList, deptPostVo);
                returnList.add(deptPostVo);
            }
        }
        if (returnList.isEmpty()) {
            returnList = deptPostList;
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<deptPostVo> list, deptPostVo t) {
        // 得到子节点列表
        List<deptPostVo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (deptPostVo tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<deptPostVo> getChildList(List<deptPostVo> list, deptPostVo t) {
        List<deptPostVo> tList = new ArrayList<deptPostVo>();
        Iterator<deptPostVo> it = list.iterator();
        while (it.hasNext()) {
            deptPostVo n = (deptPostVo) it.next();
            if (StringUtils.isNotNull(n.getFUid()) && n.getFUid().longValue() == t.getUid().longValue()) {
                tList.add(n);
            }
        }
        return tList;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<deptPostVo> list, deptPostVo t) {
        return getChildList(list, t).size() > 0;
    }
}