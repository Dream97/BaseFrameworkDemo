package com.rastargame.rick.read.model.entity;

/**
 * 书籍信息
 * Author: Rick
 * Email: zhiyuanfeng@rastar.com
 * Date: 2019/1/14
 */
public class BookEntity {
    private String bookName;
    private String headerImg;
    private String AuthorName;
    private int res;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }
}
