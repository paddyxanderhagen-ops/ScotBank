$(document).ready(function () {
    let type = $('#transaction-type').val();
    let amount = $('#amount').val();
    let form = $('#transaction-form');

    form.on('submit', function (e) {
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
    $.ajax({
        url: 'dashboard/transaction/deposit',
        method: 'POST',
        headers: {
            'Accept': 'application/json'
        },
        data: {
            amount: $("#amount").val()
        },
        success: function (data,status) {
            if (status === "success") {
                window.location.href='/dashboard';
            }
        },
        error: function (xhr) {
            renderError(xhr);
        }
    });
}

function withdraw() {
    $.ajax({
        url: 'dashboard/transaction/withdraw',
        method: 'POST',
        headers: {
            'Accept': 'application/json'
        },
        data: {
            amount: $("#amount").val()
        },
        success: function (data,status) {
            if (status === "success") {
                window.location.href='/dashboard';
            }
        },
        error: function (xhr) {
            renderError(xhr);
        }
    });
}

function renderError(xhr) {
    const { error, message } = xhr.responseJSON;
    $("#error").html(`
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>Error:</strong> ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `);
}