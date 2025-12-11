// 刷新表格
function refreshNewsTable() {
    $.ajax({
        type: 'GET',
        url: '/list',
        success: function (response) {
            // 从响应中提取表格内容
            var tableHtml = $(response).find('#news-table-container').html();
            // 更新当前页面的表格
            $('#news-table-container').html(tableHtml);
            // 重新格式化所有时间戳
            formatAllTimestamps();
        },
        error: function (error) {
            console.error('Error refreshing table:', error);
        }
    });
}

$(document).ready(function() {
    // 页面加载完成后格式化所有时间戳
    formatAllTimestamps();
});

// 格式化所有时间戳元素
function formatAllTimestamps() {
    var timestampElements = document.querySelectorAll('.timestamp');
    timestampElements.forEach(function(element) {
        var timestamp = parseInt(element.textContent);
        var formattedDate = formatTimestamp(timestamp);
        element.textContent = formattedDate;
    });
}

// 删除新闻
function deleteNews(id) {
    $.post('/delete/' + id, function () {
        // 删除成功后刷新表格
        refreshNewsTable();
    });
}

// 格式化时间戳为日期字符串
function formatTimestamp(timestamp) {
    var date = new Date(timestamp);
    var year = date.getFullYear();
    var month = ('0' + (date.getMonth() + 1)).slice(-2);
    var day = ('0' + date.getDate()).slice(-2);
    var hours = ('0' + date.getHours()).slice(-2);
    var minutes = ('0' + date.getMinutes()).slice(-2);
    var seconds = ('0' + date.getSeconds()).slice(-2);
    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
}