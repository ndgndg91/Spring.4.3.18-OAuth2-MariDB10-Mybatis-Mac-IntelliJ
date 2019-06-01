var CommonCtrl = {
    commonAjax : function (url, method, data, successCallback, failCallback) {
        $.ajax({
            url : "http://localhost:8080"+url,
            method: method,
            data: data,
            dataType : "json",
            success : function (msg) {
                console.log(msg);
                if (successCallback)
                    successCallback(msg);
            },
            error : function (jqXHR, testStatus, errorThrown) {
                console.log(jqXHR);
                if (failCallback)
                    failCallback();
            }
        });
    }
}