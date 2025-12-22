$(document).ready(function () {
    // 爬取按钮
    $('#crawlBtn').on('click', function crawlNews() {
        var startPage = $('#startPage').val();
        var endPage = $('#endPage').val();
        var resultDiv = $('#result');

        resultDiv.text('正在爬取中...');
        resultDiv.css('background-color', '#e9ecef');
        resultDiv.show();

        $.post('/spider/crawl', { startPage: startPage, endPage: endPage }, function (data) {
            resultDiv.text(data);
            if (data.indexOf('成功') >= 0) {
                resultDiv.css('background-color', '#d4edda');
            } else {
                resultDiv.css('background-color', '#f8d7da');
            }
        }).fail(function () {
            resultDiv.text('请求失败，请稍后重试');
            resultDiv.css('background-color', '#f8d7da');
        });
    });

    // 清空按钮
    $('#clearBtn').on('click', function clearNews() {
    var resultDiv = $('#result');

    resultDiv.text('正在清空数据...');
    resultDiv.css('background-color', '#e9ecef');
    resultDiv.show();

    $.post('/spider/clear', function (data) {
        resultDiv.text(data);
        if (data.indexOf('成功') >= 0) {
            resultDiv.css('background-color', '#d4edda');
        } else {
            resultDiv.css('background-color', '#f8d7da');
        }
    }).fail(function () {
        resultDiv.text('请求失败，请稍后重试');
        resultDiv.css('background-color', '#f8d7da');
    });
});
});
