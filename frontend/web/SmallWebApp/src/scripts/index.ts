class Person {
    id: string;
    firstName: string;
    lastName: string;
    email: string;

    constructor(id: string, firstName: string, lastName: string, email: string) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}

interface PersonBlueprint {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
}

let totalButton: HTMLButtonElement = <HTMLButtonElement>document.getElementById("showAllUsers");
totalButton.addEventListener("click", () => displayPeople());

async function loadPeople(): Promise<Person[]> {
    let people: Person[] = [];
    let url: string = "http://localhost:8080/user";
    let response: Response = await fetch(url);
    let peopleBlueprints: PersonBlueprint[] = JSON.parse(await response.text());
    for (let i: number = 0; i < peopleBlueprints.length; i++) {
        //Mapping json data to object. If numbers are used, cast to int with parseInt
        let person: Person = new Person(peopleBlueprints[i].id, peopleBlueprints[i].firstName, peopleBlueprints[i].lastName, peopleBlueprints[i].email);
        people.push(person);
    }
    return people;
}


async function getEmailsOfUsersAsText(): Promise<string> {
    console.log("Get user emails....");
    let people: Person[] = await loadPeople();
    let text: string = "<div class=\"flex one two-1000 three-1400 six-1600 demo center\">";
    people.forEach(p => text += getProfileAsHtml(p) + "<br/>");
    text = text + "</div>";
    return text;
}

function displayPeople() {
// @ts-ignore
    getEmailsOfUsersAsText().then(text => document.getElementById("users").innerHTML = text);
}

function getProfileAsHtml(person: Person) {
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

