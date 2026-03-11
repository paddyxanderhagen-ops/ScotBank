$(document).ready(function () {
    let type = $('#transaction-type').val();
    let amount = $('#amount').val();
    let form = $('#transactionForm');

    form.on('submit',function (e) {
       e.preventDefault();
        type = $('#transaction-type').val();
        amount = $('#amount').val();

        if (type === "deposit" && amount !== "") {
            deposit();
        } else if (type === "withdraw" && amount !== "") {
            withdraw();
        }
    });
});

function deposit() {
    let posting = $.post(`dashboard/deposit`,
        {
            amount: $("#amount").val()
        }, function (data, response) {
            if (response === "success") {
                window.location.href=`/dashboard`;
            }
        }
    );

    posting.fail(function(xhr) {
        $('#amount-error').html(xhr.responseJSON).show();
    });
}

function withdraw () {
    let posting = $.post(`dashboard/withdraw`,
        {
            amount: $("#amount").val()
        }, function (data, status) {
            window.location.href=`dashboard`;
        }
    );

    posting.fail(function(xhr) {
        $('#amount-error').html(xhr.responseJSON).show();
    });
}

function openCloseForm () {
    $('#transactionForm').toggleClass("d-none");
}