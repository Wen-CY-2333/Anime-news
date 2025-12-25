$(document).ready(function() {
    // 搜索框按回车搜索
    $('#search-input').on('keypress', function(e) {
        if (e.which === 13 || e.keyCode === 13) {
            $(this).closest('form').submit();
        }
    });
});
