$(document).ready(function () {
    $('#transactionForm').hide();
    $('#transactionForm').on('submit',function (e) {
       e.preventDefault();
        var type = $('#transaction-type').val();
        var amount = $('#amount').val();

        if (type === "deposit" && amount !== "") {
            deposit();
        } else if (type === "withdraw" && amount !== "") {
            withdraw();
        }
    });
});

function deposit() {
    let id = window.location.pathname;
    $.post(`${id}/deposit`,
        {
            amount: $("#amount").val()
        }, function (data, response) {
            if (response === "success") {
                window.location.href=`${id}`;
            } else {
                alert("Submission Failed");
            }
        }
    );
}

function withdraw () {
    let id = window.location.pathname;
    var posting = $.post(`${id}/withdraw`,
        {
            amount: $("#amount").val()
        }, function (data, status) {
            console.log(status);
            window.location.href=`${id}`;
        }
    )

    posting.fail(
        function (xhr) {
            alert(JSON.parse(xhr.responseText));
        }
    );
}

function openForm () {
    $('#transactionForm').show();
}

function closeForm () {
    $('#amount').val("");
    $('#transactionForm').hide();
}