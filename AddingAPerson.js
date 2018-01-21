var mysql = require('mysql');

var con = mysql.createConnection({
    host : "localhost",
    user: "root",
    database: "CyCap"
})

function addPerson(name, password){
    con.connect(function(err){
        if(err) throw err;
        var sql = "INSERT INTO user (username, password) VALUES ('" +name + "', '" + password + "')";
        con.query(sql, function(err, result){
            if(err) throw err;
            console.log("person added");
        })
    })
}