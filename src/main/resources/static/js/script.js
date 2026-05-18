document.addEventListener('DOMContentLoaded', function () {

    var sidebarToggle = document.getElementById('sidebarToggle');
    var sidebar = document.getElementById('sidebar');

    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function () {
            sidebar.classList.toggle('active');
        });
    }

    var alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            var closeBtn = alert.querySelector('.btn-close');
            if (closeBtn) {
                closeBtn.click();
            } else {
                alert.style.transition = 'opacity 0.5s';
                alert.style.opacity = '0';
                setTimeout(function () {
                    alert.style.display = 'none';
                }, 500);
            }
        }, 5000);
    });

    var searchInputs = document.querySelectorAll('.table-search');
    searchInputs.forEach(function (input) {
        input.addEventListener('keyup', function () {
            var searchTerm = this.value.toLowerCase().trim();
            var tableContainer = this.closest('.table-responsive');
            if (!tableContainer) return;
            var table = tableContainer.querySelector('table');
            if (!table) return;
            var rows = table.querySelectorAll('tbody tr');
            rows.forEach(function (row) {
                var text = row.textContent.toLowerCase();
                row.style.display = text.includes(searchTerm) ? '' : 'none';
            });
        });
    });

    var deleteButtons = document.querySelectorAll('.btn-delete-confirm');
    deleteButtons.forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            if (!confirm('Apakah Anda yakin ingin menghapus data ini?')) {
                e.preventDefault();
            }
        });
    });

    var confirmButtons = document.querySelectorAll('.btn-confirm-action');
    confirmButtons.forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            var message = this.getAttribute('data-confirm') || 'Apakah Anda yakin?';
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });

    var currentPath = window.location.pathname;
    var navLinks = document.querySelectorAll('.sidebar-nav .nav-link');
    navLinks.forEach(function (link) {
        var href = link.getAttribute('href');
        if (href && currentPath.indexOf(href) !== -1) {
            navLinks.forEach(function (l) { l.classList.remove('active'); });
            link.classList.add('active');
        }
    });

    var filterTabs = document.querySelectorAll('.filter-tab');
    filterTabs.forEach(function (tab) {
        tab.addEventListener('click', function (e) {
            e.preventDefault();
            filterTabs.forEach(function (t) { t.classList.remove('active'); });
            this.classList.add('active');
            var filter = this.getAttribute('data-filter');
            var targetRows = document.querySelectorAll(this.getAttribute('data-target') + ' tbody tr');
            targetRows.forEach(function (row) {
                if (filter === 'all' || row.getAttribute('data-role') === filter) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
    });

});
