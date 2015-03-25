package org.savvas.shafted.controller.request;


public class GroupRequest {

    private Long id;
    private String name;

    public GroupRequest() {
    }

    public GroupRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
