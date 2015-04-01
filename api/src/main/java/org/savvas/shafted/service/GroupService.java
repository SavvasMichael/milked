package org.savvas.shafted.service;


import org.savvas.shafted.controller.request.GroupRequest;
import org.savvas.shafted.domain.GroupUser;
import org.savvas.shafted.domain.Shaft;
import org.savvas.shafted.domain.ShaftGroup;
import org.savvas.shafted.domain.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    private GroupRepository groupRepository;
    private EntityManager em;

    GroupService() {
    }

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Long createGroup(GroupRequest groupRequest) {
        ShaftGroup shaftGroup = new ShaftGroup(groupRequest.getName(), groupRequest.getUserId());
        ShaftGroup savedShaftGroup = groupRepository.save(shaftGroup);
        return savedShaftGroup.getId();
    }

    public ShaftGroup getGroup(Long id) {
        return groupRepository.findById(id);
    }

    public List<ShaftGroup> getUserGroups(Long id){
        List<ShaftGroup> userGroups;
        TypedQuery<ShaftGroup> query = em.createQuery("SELECT * FROM ShaftGroup WHERE userId = " + id, ShaftGroup.class);
        userGroups = query.getResultList();
        return userGroups;
    }

}
