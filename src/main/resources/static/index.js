const headers = {
    'Content-Type': 'application/json'
};

async function get_user() {
    const response = await fetch("http://localhost:8080/api/v1/users/get-logged-in-user", {
        method: "POST",
        body: {},
        headers: headers
    }).catch(err => console.error(err.message));

    if (response.status === 200) {
        usr = await response.json();
        return usr;
    }
}

async function get_grocery_lists() {
    const response = await fetch("http://localhost:8080/api/v1/glist/get-user-glists", {
        method: "POST",
        body: {},
        headers: headers
    }).catch(err => console.error(err.message));

    if (response.status === 200) {
        gLists = await response.json();
        return gLists;
    }
}

async function create_grocery_list() {
    const response = await fetch("http://localhost:8080/api/v1/glist/create", {
        method: "POST",
        body: `${(await get_user()).username}+${prompt("Enter the name: ")}`,
        headers: headers
    }).catch(err => console.error(err.message));

    if (response.status === 200) {
        await disp_grocery_lists();
        return await response.json();
    };
}

async function delete_grocery_list(g_list) {
    glist = {
        name: `${(await get_user()).username}+${g_list}`
    }
    const response = await fetch("http://localhost:8080/api/v1/glist/delete", {
        method: "POST",
        body: JSON.stringify(glist),
        headers: headers
    }).catch(err => console.error(err.message));

    if (response.status === 200) {
        await disp_grocery_lists();
        return await response.json();
    };
}

async function disp_grocery_lists() {
    var g_lists = await get_grocery_lists();

    var g_list = document.getElementById("glist-list");
    while (g_list.hasChildNodes()) {
        g_list.removeChild(g_list.firstChild);
    }

    for (var list of g_lists) {
        var item_li = document.createElement("li");
        g_list.appendChild(item_li);
        item_li.className = "list-group-item row";
        item_li.style = "margin-left: -10px";

        var row = document.createElement("div");
        item_li.appendChild(row);
        row.className = "row";

        var quantity = document.createElement("p");
        row.appendChild(quantity);
        quantity.className = "col-2";
        quantity.style = "padding-left: 35px;"
        quantity.innerHTML = list.itemsList.length.toString();

        var name = document.createElement("a");
        row.appendChild(name);
        name.className = "col";
        name.href = `grocery-list/${list.listID}`;
        console.log(list);
        name.innerHTML = list.name.split("+")[1];

        var delete_btn = document.createElement("btn");
        row.appendChild(delete_btn);
        delete_btn.innerHTML = "&times";
        delete_btn.className = "close border-0";
        delete_btn.style = "cursor: pointer;";
        delete_btn.addEventListener("click", async(e) => {
            await delete_grocery_list(e.target.parentElement.children[1].innerHTML);
        });
    }
}

document.onload = disp_grocery_lists();
document.getElementById("create-glist").addEventListener("click", async() => {
    await create_grocery_list();
});