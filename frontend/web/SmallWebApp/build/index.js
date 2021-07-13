"use strict";
class Post {
    constructor(id, content, color, author, time, comments) {
        this.id = id;
        this.content = content;
        this.color = color;
        this.author = author;
        this.time = time;
        this.comments = comments;
    }
}
class PostComment {
    constructor(id, content, author, time) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.time = time;
    }
}
class Author {
    constructor(id, name) {
        this.id = id;
        this.name = name;
    }
}
const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const id = urlParams.get('id');
if (id != null && id.length == 36) {
    displayPost();
}
async function loadPosts() {
    let url = "http://localhost:8080/post/" + id;
    let response = await fetch(url);
    let postBlueprint = JSON.parse(await response.text());
    let authorBlueprint = postBlueprint.author;
    let author = new Author(authorBlueprint.id, authorBlueprint.name);
    let postCommentBluePrints = postBlueprint.comments;
    let postComments = [];
    postCommentBluePrints.forEach(cbp => {
        let postCommentAuthorBlueprint = cbp.author;
        let postCommentAuthor = new Author(postCommentAuthorBlueprint.id, postCommentAuthorBlueprint.name);
        postComments.push(new PostComment(cbp.id, cbp.content, postCommentAuthor, cbp.time));
    });
    //Mapping json data to object. If numbers are used, cast to int with parseInt
    return new Post(postBlueprint.id, postBlueprint.content, postBlueprint.color, author, postBlueprint.time, postComments); //, postBlueprint.comments);
}
async function getPost() {
    console.log("Get user emails....");
    let post = await loadPosts();
    let text = "<div class=\"flex one two-1000 three-1400 six-1600 demo center\">";
    text += getPostAsHtml(post) + "<br/></div>";
    return text;
}
function displayPost() {
    // @ts-ignore
    getPost().then(text => document.getElementById("post").innerHTML = text);
}
function convertTime(unix) {
    const date = new Date(unix * 1000);
    let day = date.getDate();
    let month = date.getMonth();
    month = month + 1; // january is 0
    let fullYear = date.getFullYear();
    const hours = date.getHours();
    const minutes = "0" + date.getMinutes();
    return day + "." + month + "." + fullYear + ", " + hours + ':' + minutes.substr(-2);
}
function getPostAsHtml(post) {
    return "<div>" +
        "    <article class=\"card\" style='background-color: " + post.color + "'>" +
        "      <footer>" +
        "        <h6 style=\"text-align:left;\">" +
        "@" + post.author.name +
        "         <span style=\"float:right;\">" +
        convertTime(post.time) +
        "          </span>" +
        "         </h6>" +
        "        <p>" + post.content + "</p>" +
        "      </footer>" +
        getCommentsAsHtml(post) +
        "    </article>" +
        "  </div>";
}
function getCommentsAsHtml(post) {
    let commentsHtml = "";
    post.comments.forEach(postComment => {
        commentsHtml += "<hr class=\"big-divider\"><div>" +
            "      <footer>" +
            "        <h6 style=\"text-align:left;\">" +
            "@" + postComment.author.name +
            "         <span style=\"float:right;\">" +
            convertTime(postComment.time) +
            "          </span>" +
            "         </h6>" +
            "        <p>" + postComment.content + "</p>" +
            "      </footer>" +
            "  </div>";
    });
    return commentsHtml;
}
//# sourceMappingURL=index.js.map