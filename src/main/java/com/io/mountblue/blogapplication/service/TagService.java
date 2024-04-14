package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAllTags();

    Tag findTagByName(String name);

    void saveTag(Tag tag);

    List<Tag> tagHandlerInDb(String tags);
}
