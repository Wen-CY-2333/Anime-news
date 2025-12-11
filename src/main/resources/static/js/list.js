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
        },
        error: function (error) {
            console.error('Error refreshing table:', error);
        }
    });
}

// 删除新闻
function deleteNews(id) {
    $.post('/delete/' + id, function () {
        // 删除成功后刷新表格
        refreshNewsTable();
        setTimeout(function () {
            alert('删除成功');
        }, 100);// 添加延时，删除并刷新后显示成功提示
    });
}