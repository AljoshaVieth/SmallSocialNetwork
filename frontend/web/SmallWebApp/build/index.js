"use strict";
class Person {
    constructor(id, firstName, lastName, email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
let totalButton = document.getElementById("showAllUsers");
totalButton.addEventListener("click", () => displayPeople());
async function loadPeople() {
    let people = [];
    let url = "http://localhost:8080/user";
    let response = await fetch(url);
    let peopleBlueprints = JSON.parse(await response.text());
    for (let i = 0; i < peopleBlueprints.length; i++) {
        //Mapping json data to object. If numbers are used, cast to int with parseInt
        let person = new Person(peopleBlueprints[i].id, peopleBlueprints[i].firstName, peopleBlueprints[i].lastName, peopleBlueprints[i].email);
        people.push(person);
    }
    return people;
}
async function getEmailsOfUsersAsText() {
    console.log("Get user emails....");
    let people = await loadPeople();
    let text = "<div class=\"flex one two-1000 three-1400 six-1600 demo center\">";
    people.forEach(p => text += getProfileAsHtml(p) + "<br/>");
    text = text + "</div>";
    return text;
}
function displayPeople() {
    // @ts-ignore
    getEmailsOfUsersAsText().then(text => document.getElementById("users").innerHTML = text);
}
function getProfileAsHtml(person) {
    return "<div>\n" +
        "    <article class=\"card\">\n" +
        "      <img class=\"profileimage\"src=\"https://source.unsplash.com/collection/2219444/600x600?" + person.id + "\">\n" +
        "      <footer>\n" +
        "        <h3>" + person.firstName + " " + person.lastName + "</h3>\n" +
        "        <p>" + person.email + "</p>\n" +
        "      </footer>\n" +
        "    </article>\n" +
        "  </div>";
}
//# sourceMappingURL=index.js.map