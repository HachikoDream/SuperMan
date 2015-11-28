package com.dreamspace.superman.model.api;

import java.util.List;

/**
 * Created by Wells on 2015/11/28.
 */
public class CollectRes {
    private List<CollectLessonInfo> lessons;

    public List<CollectLessonInfo> getLessons() {
        return lessons;
    }

    public void setLessons(List<CollectLessonInfo> lessons) {
        this.lessons = lessons;
    }

    public class CollectLessonInfo {
        private int collection_count;
        private String keeptime;
        private String less_id;
        private String name;
        private String tags;
        private String image;
        private int price;
        private String state;
        private int success_count;
        private int mas_id;
        private String less_name;

        public int getCollection_count() {
            return collection_count;
        }

        public void setCollection_count(int collection_count) {
            this.collection_count = collection_count;
        }

        public String getKeeptime() {
            return keeptime;
        }

        public void setKeeptime(String keeptime) {
            this.keeptime = keeptime;
        }

        public String getLess_id() {
            return less_id;
        }

        public void setLess_id(String less_id) {
            this.less_id = less_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getSuccess_count() {
            return success_count;
        }

        public void setSuccess_count(int success_count) {
            this.success_count = success_count;
        }

        public int getMas_id() {
            return mas_id;
        }

        public void setMas_id(int mas_id) {
            this.mas_id = mas_id;
        }

        public String getLess_name() {
            return less_name;
        }

        public void setLess_name(String less_name) {
            this.less_name = less_name;
        }
    }
}
