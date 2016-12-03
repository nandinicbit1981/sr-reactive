$(document).ready(function() {

    $('#classSelect').val($("#classSelect").data("value"));
    $('#raceSelect').val($("#raceSelect").data("value"));
    
    $("#strength_stat").on("change",function(){
        var value = $("#strength_stat").val();
        var mod = (value - 11)/2;
        console.log(mod.toFixed(2));
        $("#strength_mod").val(mod.toFixed(2));
    });

    $("#dex_stat").on("change",function(){
        var value = $("#dex_stat").val();
        var mod = (value - 11)/2;
        console.log(mod.toFixed(2));
        $("#dex_mod").val(mod.toFixed(2));
        $("#ac").val(parseInt(value)+10);
    });

    $("#con_stat").on("change",function(){
        var value = $("#con_stat").val();
        var mod = (value - 11)/2;
        console.log(mod.toFixed(2));
        $("#con_mod").val(mod.toFixed(2));

    });

    $("#wsdm_stat").on("change",function(){
        var value = $("#wsdm_stat").val();
        var mod = (value - 11)/2;
        console.log(mod.toFixed(2));
        $("#wsdm_mod").val(mod.toFixed(2));
    });

    $("#intl_stat").on("change",function(){
        var value = $("#intl_stat").val();
        var mod = (value - 11)/2;
        console.log(mod.toFixed(2));
        $("#intl_mod").val(mod.toFixed(2));
    });

    $("#charisma_stat").on("change",function(){
        var value = $("#charisma_stat").val();
        var mod = (value - 11)/2;
        console.log(mod.toFixed(2));
        $("#charisma_mod").val(mod.toFixed(2));
    });


    $("#submitBtn").click(function () {

        var name = $("#name").val();
        var className = $("#classSelect").val();
        var raceName = $("#raceSelect").val();
        var strengthStat = $("#strength_stat").val() != "" ? $("#strength_stat").val() : 0;
        var dextirityStat = $("#dex_stat").val() != "" ? $("#dex_stat").val() : 0;
        var constitutionStat = $("#con_stat").val() != "" ? $("#con_stat").val() : 0;
        var intlStat = $("#intl_stat").val() != "" ? $("#intl_stat").val() : 0;
        var wsdmStat = $("#wsdm_stat").val() != "" ? $("#wsdm_stat").val() : 0;
        var charismaStat = $("#charisma_stat").val() != "" ? $("#charisma_stat").val() : 0;
        var strengthMod = $("#strength_mod").val() != "" ? $("#strength_mod").val() : 0;
        var dextirityMod = $("#dex_mod").val() != "" ? $("#dex_mod").val() : 0;
        var constitutionMod = $("#con_mod").val() != "" ? $("#con_mod").val() : 0;
        var intlMod = $("#intl_mod").val() != "" ? $("#intl_mod").val() : 0;
        var wsdmMod = $("#wsdm_mod").val() != "" ? $("#wsdm_mod").val() : 0;
        var charismaMod = $("#charisma_mod").val() != "" ? $("#charisma_mod").val() : 0;
        var ac = $("#ac").val() != "" ? $("#ac").val() : 0;

        var charInst = new CharacterWithoutName(name, className,raceName,
                            strengthStat, dextirityStat, constitutionStat,
                            intlStat, wsdmStat, charismaStat,
                            strengthMod, dextirityMod, constitutionMod,
                            intlMod, wsdmMod, charismaMod, ac);

        var params = JSON.stringify(charInst);
        create_character("/characters", params);
        window.location.href = "/characters";
    });

    $("#editSubmitBtn").click(function() {
        var name = $("#name").val();
        var className = $("#classSelect").val();
        var raceName = $("#raceSelect").val();
        var strengthStat = $("#strength_stat").val() != "" ? $("#strength_stat").val() : 0;
        var dextirityStat = $("#dex_stat").val() != "" ? $("#dex_stat").val() : 0;
        var constitutionStat = $("#con_stat").val() != "" ? $("#con_stat").val() : 0;
        var intlStat = $("#intl_stat").val() != "" ? $("#intl_stat").val() : 0;
        var wsdmStat = $("#wsdm_stat").val() != "" ? $("#wsdm_stat").val() : 0;
        var charismaStat = $("#charisma_stat").val() != "" ? $("#charisma_stat").val() : 0;
        var strengthMod = $("#strength_mod").val() != "" ? $("#strength_mod").val() : 0;
        var dextirityMod = $("#dex_mod").val() != "" ? $("#dex_mod").val() : 0;
        var constitutionMod = $("#con_mod").val() != "" ? $("#con_mod").val() : 0;
        var intlMod = $("#intl_mod").val() != "" ? $("#intl_mod").val() : 0;
        var wsdmMod = $("#wsdm_mod").val() != "" ? $("#wsdm_mod").val() : 0;
        var charismaMod = $("#charisma_mod").val() != "" ? $("#charisma_mod").val() : 0;
        var ac = $("#ac").val() != "" ? $("#ac").val() : 0;
        var id = $("#editSubmitBtn").data("id");
        var charInst = new Character(id, name, className,raceName,
            strengthStat, dextirityStat, constitutionStat,
            intlStat, wsdmStat, charismaStat,
            strengthMod, dextirityMod, constitutionMod,
            intlMod, wsdmMod, charismaMod, ac);
        var params = JSON.stringify(charInst);

        edit_character("/characters/"+id, params);
        window.location.href = "/characters";

    });

    $("#deleteCharacter").click(function() {
        var id = $("#deleteCharacter").data("id");
        delete_character("/characters/"+id);

    });

    $(".view-character").click(function() {
        var id = $(this).data("id");
        get_character("/character/id", id);
        // var element = $(".view-character").data("id");

    });

    function CharacterWithoutName(name, className,raceName,
                       strengthStat, dextirityStat, constitutionStat,
                       intlStat, wsdmStat, charismaStat,
                       strengthMod, dextirityMod, constitutionMod,
                       intlMod, wsdmMod, charismaMod, ac) {

        var character = {
            name: name,
            className: className,
            race: raceName,
            strength_stat: strengthStat,
            dex_stat: dextirityStat,
            con_stat: constitutionStat,
            intl_stat: intlStat,
            wsdm_stat: wsdmStat,
            chr_stat: charismaStat,
            strength_mod: strengthMod,
            dex_mod: dextirityMod,
            con_mod:constitutionMod,
            intl_mod:intlMod,
            wsdm_mod: wsdmMod,
            chr_mod: charismaMod,
            ac: ac
        };


        return character;
    }

    function Character(id, name, className,raceName,
                       strengthStat, dextirityStat, constitutionStat,
                       intlStat, wsdmStat, charismaStat,
                       strengthMod, dextirityMod, constitutionMod,
                       intlMod, wsdmMod, charismaMod, ac) {

        var character = {
            id:id,
            name: name,
            className: className,
            race: raceName,
            strength_stat: strengthStat,
            dex_stat: dextirityStat,
            con_stat: constitutionStat,
            intl_stat: intlStat,
            wsdm_stat: wsdmStat,
            chr_stat: charismaStat,
            strength_mod: strengthMod,
            dex_mod: dextirityMod,
            con_mod:constitutionMod,
            intl_mod:intlMod,
            wsdm_mod: wsdmMod,
            chr_mod: charismaMod,
            ac: ac
        };

        return character;
    }

    function create_character(url,params){
        //  var http = new XMLHttpRequest();
        //  var url = url;
        //  var params = params;
        //  http.setRequestHeader("Content-type", "application/json");
        //  http.open("PUT", url, true);
        //  http.onreadystatechange = function() {
        //      //Call a function when the state changes.
        //      if(http.readyState == 4 && http.status == 200) {
        //            console.log(http.responseText);
        //              //window.location.href = "/characters";
        //      }
        //  };
        // http.send(params);

            $.ajax({
                type: "PUT",
                url: url,
                contentType : "application/json; charset=utf-8",
                dataType: "json",
                data: params,
                success: function(){
                    window.location.href = "/characters";
                }
            });
    };


    function edit_character(url,params){
        $.ajax({
            type: "POST",
            url: url,
            contentType : "application/json; charset=utf-8",
            dataType: "json",
            data: params,
            success: function(){
                window.location.href = "/characters";
            }
        });
        // var http = new XMLHttpRequest();
        // var url = url;
        // var params = params;
        // http.open("POST", url, true);
        // http.onreadystatechange = function() {
        //     //Call a function when the state changes.
        //     if(http.readyState == 4 && http.status == 200) {
        //         console.log(http.responseText);
        //         window.location.href = "/characters";
        //     }
        // }
        // http.send(params);
    };
    function get_character(url,params){
        var http = new XMLHttpRequest();
        var url = url;
        var params = params;
        http.open("GET", url, true);
        http.onreadystatechange = function() {
            //Call a function when the state changes.
            if(http.readyState == 4 && http.status == 200) {
                console.log(http.responseText);
                window.location.href = "characters";
            }
        }
        http.send(params);
    };

    function delete_character(url){
        // var http = new XMLHttpRequest();
        // var url = url;
        // http.open("DELETE", url, true);
        // http.onreadystatechange = function() {
        //     //Call a function when the state changes.
        //     if(http.readyState == 4 && http.status == 200) {
        //         console.log(http.responseText);
        //         window.location.href = "../";
        //     }
        // };
        // http.send();

        $.ajax({
            type: "DELETE",
            url: url,
            contentType : "application/json; charset=utf-8",
            dataType: "json",
            success: function(){
                window.location.href = "/characters";
            }
        });
    };
    
});