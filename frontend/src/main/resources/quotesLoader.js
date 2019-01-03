function depsHtml(deps) {
    let html = `<ul>`;
    for (let i = 0; i < deps.length; i++) {
        const dep = `<li>
                <b>Token: </b> ${deps[i]['token']} <br>
                <b>POF: </b> ${deps[i]['pof']} <br>
                <b>Type: </b> ${deps[i]['dep type']} <br>
                </li>`;
        html = html + dep;
    }
    html = html + `</ul>`;

    return html;
}

function neighborsHtml(neighbors) {
    let html = `<ul>`;
    for (let i = 0; i < neighbors.length; i++) {
        const dep = `<li>
                <b>Token: </b> ${neighbors[i]['token']} <br>
                <b>POF: </b> ${neighbors[i]['pof']} <br>
                </li>`;
        html = html + dep;
    }
    html = html + `</ul>`;

    return html;
}

function addLocation(locationContext) {
    const title = locationContext['title'];
    const author = locationContext['author'];
    const type = locationContext['type'];
    const userRating = locationContext['user rating'];
    const amodDeps = locationContext['amod deps'];
    const amodNeighbors = locationContext['amod neighbors'];
    const reviewsNumber = locationContext['reviews number'];
    const location = locationContext['location'];
    const quote = locationContext['sentence'];
    const inDeps = locationContext['incoming deps'];
    const outDeps = locationContext['outgoing deps'];
    const leftNeighbors = locationContext['left neighbors'];
    const rightNeighbors = locationContext['right neighbors'];

    const listItem = `<li>
            <b>Title: </b> ${title} <br>
            <b>Author: </b> ${author} <br>
            <b>Type: </b> ${type} <br>
            <b>User rating: </b> ${userRating} <br>
            <b>Amod deps: </b> ${amodDeps} <br>
            <b>Amod neighbors: </b> ${amodNeighbors} <br>
            <b>Reviews number: </b> ${reviewsNumber} <br>
            <b>Location: </b> ${location} <br>
            ${quote} <br>
            <b>Incoming dependencies: </b> ${depsHtml(inDeps)}
            <b>Outgoing dependencies: </b> ${depsHtml(outDeps)}
            <b>Left neighbors: </b> ${neighborsHtml(leftNeighbors)}
            <b>Right neighbors: </b> ${neighborsHtml(rightNeighbors)}
            </li>`;

    $(".main").append(listItem);
}

function load (query) {
    $.get(query, {}, function (response) {
        const locations = JSON.parse(response);
        const locationsArray = locations['locations'];
        for (let i = 0; i < locationsArray.length; i++) {
            addLocation(locationsArray[i]);
        }
    });
}