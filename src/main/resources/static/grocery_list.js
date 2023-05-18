const headers = {
    'Content-Type': 'application/json'
};

async function get_items() {
    var path = window.location.pathname.split("/");
    let gListID = Number(path[path.length - 1]);

    const response = await fetch("http://localhost:8080/api/v1/glist/get_glist", {
        method: "POST",
        body: gListID,
        headers: headers
    }).catch(err => console.error(err.message));

    if (response.status === 200) {
        var resp = await response.json();
        return resp;
    };
}

async function create_item() {
    var path = window.location.pathname.split("/");
    let gListID = Number(path[path.length - 1]);

    var item = {
        itemName: prompt("Enter Item Name Search Query: "),
        itemPrice: 0,
        itemQuantity: prompt("Enter Item Quantity: "),
        walmartSKU: 0
    }

    const response = await fetch(`http://localhost:8080/api/v1/glist/add-item/${gListID}`, {
        method: "POST",
        body: JSON.stringify(item),
        headers: headers
    }).catch(err => console.error(err.message));

    if (response.status === 200) {
        await disp_items();
        return await response.json();
    };
}

async function delete_item(item_id) {
    var path = window.location.pathname.split("/");
    let gListID = Number(path[path.length - 1]);

    var item = {
        itemID: Number(item_id)
    }

    const response = await fetch(`http://localhost:8080/api/v1/glist/delete-item/${gListID}`, {
        method: "POST",
        body: JSON.stringify(item),
        headers: headers
    }).catch(err => console.error(err.message));

    if (response.status === 200) {
        await disp_items();
    };
}

async function disp_items() {
    var list = document.getElementById("items-list");
    var list_items = (await get_items()).itemsList;

    while (list.hasChildNodes()) {
        list.removeChild(list.firstChild);
    }

    for (var item of list_items) {
        var item_li = document.createElement("li");
        list.appendChild(item_li);
        item_li.className = "list-group-item";

        var row = document.createElement("div");
        item_li.appendChild(row);
        row.className = "row";

        var id = document.createElement("p");
        row.appendChild(id);
        id.innerHTML = `${item.itemID}`;
        id.style = "display: none;";

        var sku = document.createElement("div");
        row.appendChild(sku);
        sku.className = "col-3";
        sku.innerHTML = item.walmartSKU;

        var name = document.createElement("div");
        row.appendChild(name);
        name.className = "col-5";
        name.innerHTML = item.itemName;

        var price = document.createElement("div");
        row.appendChild(price);
        price.className = "col";
        price.innerHTML = "$" + item.itemPrice;

        var quantity = document.createElement("div");
        row.appendChild(quantity);
        quantity.className = "col";
        quantity.innerHTML = item.itemQuantity;

        var amount = document.createElement("div");
        row.appendChild(amount);
        amount.className = "col";
        amount.innerHTML = "$" + item.itemAmount;

        var delete_btn = document.createElement("btn");
        row.appendChild(delete_btn);
        delete_btn.innerHTML = "&times";
        delete_btn.className = "close border-0";
        delete_btn.style = "cursor: pointer;";
        delete_btn.addEventListener("click", async(e) => {
            var children = e.target.parentElement.children;
            await delete_item(children[0].innerHTML);
        });
    }
}

document.onload = disp_items();
document.getElementById("add-item").addEventListener("click", async() => {
    await create_item();
});