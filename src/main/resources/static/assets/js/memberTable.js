// call the get mapping to add the data to members
fetch('/member', {method: 'GET'})
.then(data=>{return data.json()})
.then(res=>{
    // the response data are the members to be displayed, call the buildtable method
    buildTable(res);
})
.catch(error=>console.log)

// used to display the rows from members to the webpage
function buildTable(data){
    // get the element from the webpage to output data to
    var table = document.getElementById('memberTable');
    // for each object in the data array, return a table row
    for(let member of data){
        var row =   `<tr>
                        <td>${member.memberid}</td>
                        <td>${member.firstname}</td>
                        <td>${member.lastname}</td>
                        <td>${member.email}</td>
                    </tr>`
        table.innerHTML += row
    }
    addscripts();
}

// add other NiceAdmin Scripts to the page after the table content has been rendered
function addscripts(){
    loadScript("assets/vendor/apexcharts/apexcharts.min.js");
    loadScript("assets/vendor/bootstrap/js/bootstrap.bundle.min.js");
    loadScript("assets/vendor/chart.js/chart.min.js");
    loadScript("assets/vendor/echarts/echarts.min.js")
    loadScript("assets/vendor/quill/quill.min.js");
    loadScript("assets/vendor/simple-datatables/simple-datatables.js");
    loadScript("assets/vendor/tinymce/tinymce.min.js");
    loadScript("assets/js/main.js");
}

// loads the script and add to the window
function loadScript(src){
    let script = document.createElement('script');
    script.src = src;
    script.async = false;
    document.body.append(script);
}