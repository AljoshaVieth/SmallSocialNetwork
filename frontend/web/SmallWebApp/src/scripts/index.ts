class Post {
    id: string;
    content: string;
    color: string;
    author: Author;
    time: number;
    comments: PostComment[];


    constructor(id: string, content: string, color: string, author: Author, time: number, comments: PostComment[]) {
        this.id = id;
        this.content = content;
        this.color = color;
        this.author = author;
        this.time = time;
        this.comments = comments;
    }
}

interface PostBlueprint {
    id: string;
    content: string;
    color: string;
    author: Author;
    time: number;
    comments: PostComment[];
}

class PostComment {
    id: string;
    content: string;
    author: Author;
    time: number;

    constructor(id: string, content: string, author: Author, time: number) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.time = time;
    }
}

interface PostCommentBlueprint {
    id: string;
    content: string;
    author: Author;
    time: number;
}

class Author {
    id: string;
    name: string;

    constructor(id: string, name: string) {
        this.id = id;
        this.name = name;
    }
}

interface AuthorBlueprint {
    id: string;
    name: string;
}
const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const id = urlParams.get('id')
if(id != null && id.length == 36){
displayPost()
}

async function loadPosts(): Promise<Post> {

    let url: string = "http://localhost:8080/post/" + id;
    let response: Response = await fetch(url);
    let postBlueprint: PostBlueprint = JSON.parse(await response.text());
    let authorBlueprint: AuthorBlueprint = postBlueprint.author;
    let author: Author = new Author(authorBlueprint.id, authorBlueprint.name);

    let postCommentBluePrints: PostCommentBlueprint[] = postBlueprint.comments;
    let postComments: PostComment[] = [];
    postCommentBluePrints.forEach(cbp => {
        let postCommentAuthorBlueprint: AuthorBlueprint = cbp.author;
        let postCommentAuthor: Author = new Author(postCommentAuthorBlueprint.id, postCommentAuthorBlueprint.name);
        postComments.push(new PostComment(cbp.id, cbp.content, postCommentAuthor, cbp.time))
    });

//Mapping json data to object. If numbers are used, cast to int with parseInt
    return new Post(postBlueprint.id, postBlueprint.content, postBlueprint.color, author, postBlueprint.time, postComments);//, postBlueprint.comments);
}


async function getPost(): Promise<string> {
    console.log("Get user emails....");
    let post: Post = await loadPosts();
    let text: string = "<div class=\"flex one two-1000 three-1400 six-1600 demo center\">";
    text += getPostAsHtml(post) + "<br/></div>"
    return text;
}

function displayPost() {
// @ts-ignore
    getPost().then(text => document.getElementById("post").innerHTML = text);
}

function convertTime(unix: number): string {
    const date = new Date(unix * 1000);
    let day = date.getDate();
    let month = date.getMonth();
    month = month + 1; // january is 0
    let fullYear = date.getFullYear();
    const hours = date.getHours();
    const minutes = "0" + date.getMinutes();
    return day + "." + month + "." + fullYear + ", " + hours + ':' + minutes.substr(-2);
}

function getPostAsHtml(post: Post) {
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

function getCommentsAsHtml(post: Post) {
    let commentsHtml: string = "";
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
    })
    return commentsHtml;
}


