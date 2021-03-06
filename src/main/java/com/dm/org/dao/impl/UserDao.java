package com.dm.org.dao.impl;


import com.dm.org.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author 刘祥德 qq313700046@icloud.com .
 * @date created in 18:11 2017/7/14.
 * @description
 * @modified by:
 */

@Repository
@SuppressWarnings("unchecked")
public class UserDao extends BaseDao<User, String>
{

    public UserDao()
    {
        super(User.class);
    }

    public User fetchUserJoinRolesById(String userId)
    {
        buildCriteriaQuery();
        baseRoot.fetch(User_.roleSet, JoinType.LEFT);
        Predicate whereRestriction = baseBuilder.equal(baseRoot.get(User_.userId), userId);
        criteriaQuery.where(whereRestriction);
        return getSession().createQuery(criteriaQuery).getSingleResult();
    }

    public User findByUserName(String userName)
    {
        buildCriteriaQuery();
        criteriaQuery.where(baseBuilder.equal(baseRoot.get(User_.userName), userName));
        return getSession().createQuery(criteriaQuery).getSingleResult();
    }

    public User fetchByUserName(String userName)
    {
        buildCriteriaQuery();
        baseRoot.fetch(User_.roleSet, JoinType.LEFT).fetch(Role_.permissionSet, JoinType.LEFT);
        criteriaQuery.where(baseBuilder.equal(baseRoot.get(User_.userName), userName));
        return getSession().createQuery(criteriaQuery).getSingleResult();
    }

    public String findPasswordByUserName(String userName)
    {
        buildCriteriaQuery();
        CriteriaQuery<String> stringCriteriaQuery = getSession().getCriteriaBuilder().createQuery(
            String.class);
        baseRoot = stringCriteriaQuery.from(User.class);
        Predicate whereRestriction = baseBuilder.equal(baseRoot.get(User_.userName), userName);
        stringCriteriaQuery.select(baseRoot.get(User_.password)).where(whereRestriction);
        return getSession().createQuery(stringCriteriaQuery).getSingleResult();
    }

    public List<Permission> findPermissionByUserName(String userName)
    {
        String hqlString = "select p from User u " + "left join u.roleSet r "
                           + "left join r.permissionSet p " + "where u.userName = :userName";
        return getSession().createQuery(hqlString).setParameter("userName",
            userName).getResultList();
    }

    public Set<String> findPermissionNameSet(String userName)
    {
        String hqlString = "select p.permissionName from User u " + "left join u.roleSet r "
                           + "left join r.permissionSet p " + "where u.userName = :userName";
        return new HashSet<String>(getSession().createQuery(hqlString).setParameter("userName",
            userName).getResultList());
    }

    public List<Role> findRolesByUserName(String userName)
    {
        buildCriteriaQuery();
        baseRoot.fetch(User_.roleSet, JoinType.LEFT);
        criteriaQuery.select(baseRoot).where(
            baseBuilder.equal(baseRoot.get(User_.userName), userName));
        User user = getSession().createQuery(criteriaQuery).getSingleResult();
        return new ArrayList<Role>(user.getRoleSet());
    }

    public Set<String> findRoleNameSetByUserName(String userName)
    {
        String hqlString = "select r.roleName from User u " + "left join u.roleSet r "
                           + "where u.userName = :userName";
        return new HashSet<String>(getSession().createQuery(hqlString).setParameter("userName",
            userName).getResultList());
    }

    public List<Long> findRoleIdListByUserId(String userId)
    {
        String hqlString = "select r.roleId from " + "User u left join u.roleSet r "
                           + "where u.userId = :userId";
        return getSession().createQuery(hqlString).setParameter("userId", userId).getResultList();
    }

    public void removeRoles(String userId, List<Long> roleIdList)
    {
        // User user = fetchUserJoinRolesById(userId);
        // for (Role role :
        // roleList)
        // {
        // user.getRoleSet().remove(role);
        // }
        String sqlString = "DELETE from user_role_re "
                           + "where userId = :userId and (roleId in :roleList)";
        getSession().createNativeQuery(sqlString).setParameter("userId", userId).setParameter(
            "roleList", roleIdList).executeUpdate();
        // buildCriteriaDelete();
        //// CriteriaDelete<Role> roleCriteriaDelete = getSession()
        //// .getCriteriaBuilder().createCriteriaDelete(Role.class);
        // // Root<Role> roleRoot = roleCriteriaDelete.from(Role.class);
        //// baseRoot.fetch(User_.roleSet,JoinType.LEFT);
        // Predicate userRestriction = baseBuilder.equal(baseRoot.get(User_.userId), userId);
        // Predicate roleRestriction = baseBuilder.in(baseRoot.get(User_.roleSet),roleList);
        // Predicate whereRestriction = baseBuilder.and(userRestriction, roleRestriction);
        // criteriaDelete.where(whereRestriction);
        // getSession().createQuery(criteriaDelete).executeUpdate();
    }

    public String getPublicSalt(String username) {
        String hqlString = "select u.publicSalt from User u "
                + "where u.userName = :username";
        return (String) getSession().createQuery(hqlString).setParameter("username", username).getSingleResult();
    }

    public String getPrivateSalt(String username) {
        String hqlString = "select u.privateSalt from User u " + "where u.userName = :username";
        return (String) getSession().createQuery(hqlString).setParameter("username", username).getSingleResult();
    }
}
