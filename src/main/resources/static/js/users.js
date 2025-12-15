$(document).ready(function () {

    // 删除用户
    $(document).on('click', '.delete-user-btn', function deleteUser() {
        var id = $(this).data('id');
        if (confirm('确定要删除这个用户吗？')) {
            $.ajax({
                type: 'POST',
                url: '/user/delete/' + id,
                success: function () {
                    // 删除成功后刷新页面
                    window.location.reload();
                },
                error: function (error) {
                    console.error('删除用户失败:', error);
                }
            });
        }
    });

    // 添加用户
    $('#saveAddUserBtn').on('click', function submitAddUser() {
        var formData = $('#addUserForm').serialize();
        $.ajax({
            type: 'POST',
            url: '/user/add',
            data: formData,
            success: function () {
                // 关闭模态框
                $('#addUserModal').modal('hide');
                // 清空表单
                $('#addUserForm')[0].reset();
                // 刷新页面
                window.location.reload();
            },
            error: function (error) {
                console.error('添加用户失败:', error);
            }
        });
    });

    // 编辑用户
    $(document).on('click', '.edit-user-btn', function editUser() {
        var id = $(this).data('id');
        $.ajax({
            type: 'GET',
            url: '/user/edit/' + id,
            success: function (user) {
                // 填充表单
                $('#editUserId').val(user.id);
                $('#editUserName').val(user.name);
                $('#editUserPassword').val(user.password);
                $('#editUserAvatar').val(user.avatar);
                $('#editUserRole').val(user.role);
            },
            error: function (error) {
                console.error('获取用户信息失败:', error);
            }
        });
    });

    // 提交编辑
    $('#saveEditUserBtn').on('click', function submitEditUser() {
        var formData = $('#editUserForm').serialize();
        $.ajax({
            type: 'POST',
            url: '/user/edit',
            data: formData,
            success: function () {
                // 关闭模态框
                $('#editUserModal').modal('hide');
                // 刷新页面
                window.location.reload();
            },
            error: function (error) {
                console.error('修改用户失败:', error);
            }
        });
    });
});


