$(document).ready(function() {
    // 删除用户按钮点击事件
    $('.delete-user-btn').click(function() {
        var id = $(this).data('id');
        if (confirm('确定要删除这个用户吗？')) {
            // 发送POST请求
            $.ajax({
                type: 'POST',
                url: '/user/delete/' + id,
                success: function() {
                    // 刷新页面
                    location.reload();
                },
                error: function(error) {
                    console.error('删除用户失败:', error);
                }
            });
        }
    });

    // 添加用户按钮点击事件
    $('#saveAddUserBtn').click(function() {
        // 获取表单数据
        var formData = {
            name: $('#addUserName').val(),
            password: $('#addUserPassword').val(),
            avatar: $('#addUserAvatar').val(),
            role: $('#addUserRole').val()
        };

        // 发送POST请求
        $.ajax({
            type: 'POST',
            url: '/user/add',
            data: formData,
            success: function() {
                // 关闭模态框
                $('#addUserModal').modal('hide');
                // 刷新页面
                location.reload();
            },
            error: function(error) {
                console.error('添加用户失败:', error);
            }
        });
    });

    // 编辑用户按钮点击事件
    $('.edit-user-btn').click(function() {
        var id = $(this).data('id');
        // 发送请求获取用户信息
        $.ajax({
            type: 'GET',
            url: '/user/find/' + id,
            success: function(user) {
                // 填充编辑表单
                $('#editUserId').val(user.id);
                $('#editUserName').val(user.name);
                $('#editUserPassword').val(user.password);
                $('#editUserAvatar').val(user.avatar);
                $('#editUserRole').val(user.role);
            },
            error: function(error) {
                console.error('获取用户信息失败:', error);
            }
        });
    });

    // 保存编辑按钮点击事件
    $('#saveEditUserBtn').click(function() {
        // 获取表单数据
        var formData = {
            id: $('#editUserId').val(),
            name: $('#editUserName').val(),
            password: $('#editUserPassword').val(),
            avatar: $('#editUserAvatar').val(),
            role: $('#editUserRole').val()
        };

        // 发送POST请求
        $.ajax({
            type: 'POST',
            url: '/user/edit',
            data: formData,
            success: function() {
                // 关闭模态框
                $('#editUserModal').modal('hide');
                // 刷新页面
                location.reload();
            },
            error: function(error) {
                console.error('修改用户失败:', error);
            }
        });
    });
});


