var mysql = require('mysql');

var con = mysql.createConnection({
    host : "localhost",
    user: "root",
    database: "CyCap"
})

con.connect(function(err){
    if (err) throw err;
    console.log("connected");
    var sql = "CREATE TABLE user (username VARCHAR(15), password VARCHAR(20))";
    con.query(sql, function(err, result){
        if(err) throw err;
        console.log("Table created");
    });
})