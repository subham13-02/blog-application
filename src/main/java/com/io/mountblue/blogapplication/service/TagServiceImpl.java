package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.dao.TagRepository;
import com.io.mountblue.blogapplication.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagServiceImpl implements TagService{

    TagRepository tagRepository;
    TagServiceImpl(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }
    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findTagByName(String name) {
        return tagRepository.findTagByName(name);
    }

    @Override
    public void saveTag(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public List<Tag> tagHandlerInDb(String tags) {
        tags = tags.toLowerCase();
        List<String> tagsInPost = Arrays.asList(tags.split(","));
        List<Tag> tagsInDB = findAllTags();
        Set<String> tagsNameInDB = new HashSet<>();
        for (Tag tag : tagsInDB) {
            tagsNameInDB.add(tag.getName());
        }

        List<Tag> postTagList = new ArrayList<>();

        for (String tag : tagsInPost) {
            Tag newTag;
            if (!tagsNameInDB.contains(tag)) {
                newTag = new Tag();
                newTag.setName(tag);
                saveTag(newTag);
            } else {
                newTag = findTagByName(tag);
            }
            postTagList.add(newTag);
        }
        return postTagList;
    }
}
