$(document).ready(function () {

    var extensions = {
        "sLength": "dataTables_length col-md-2"
    };
    $.extend($.fn.dataTableExt.oStdClasses, extensions);
    lang = {
        "sEmptyTable": "Nincs rendelkezésre álló adat",
        "sInfo": "Találatok: _START_ - _END_ Összesen: _TOTAL_",
        "sInfoEmpty": "Nulla találat",
        "sInfoFiltered": "(_MAX_ összes rekord közül szűrve)",
        "sInfoPostFix": "",
        "sInfoThousands": " ",
        "sLengthMenu": "_MENU_ oldalanként",
        "sLoadingRecords": "Betöltés...",
        "sProcessing": "Feldolgozás...",
        "sSearch": "Keresés:",
        "sZeroRecords": "Nincs a keresésnek megfelelő találat",
        "oPaginate": {
            "sFirst": "Első",
            "sPrevious": "Előző",
            "sNext": "Következő",
            "sLast": "Utolsó"
        },
        "oAria": {
            "sSortAscending": ": aktiválja a növekvő rendezéshez",
            "sSortDescending": ": aktiválja a csökkenő rendezéshez"
        }
    };

    let $todoTbl;
    loadTodoDataTable($todoTbl);

    function loadTodoDataTable($todoTbl) {
        $todoTbl = $('table#table-todo').DataTable({
            "dom": 'lBfrtip',
            'ajax': {
                'contentType': 'application/json',
                'url': '/todo/api/listTodos',
                'type': 'POST',

                'data': function (data) {
                    return JSON.stringify(data);
                }
            },
            'error': function () {
                alert("fail");
            },
            "fnCreatedRow": function( nRow) {
                $(nRow).children("td").css("overflow", "hidden");
                $(nRow).children("td").css("white-space", "nowrap");
                $(nRow).children("td").css("text-overflow", "ellipsis");
            },
            'serverSide': true,
            "language": lang,
            "responsive": true,
            "stateSave": false,
            "paging": true,
            "lengthMenu": [[10, 20, 30, 40, -1], [10, 20, 30, 40, "Összes"]],
            'pageLength': 20,
            "order": [0, "desc"],

            columnDefs: [

                {"className": "dt-center", "targets": "_all"},
                {
                    "targets": [0],
                    "visible": false,
                    "searchable": false
                },
                {
                    "targets": [ 3 ],
                    "render":function(data, type, row, meta){
                        if (row.priority == "LOW") {
                            return '<label data-column="1">LOW</label>'
                        } else if (row.priority == 'MEDIUM') {
                            return '<label data-column="2">MEDIUM</label>'

                        } else if (row.priority == 'HIGH') {
                            return '<label data-column="3">HIGH</label>'

                        }
                        else {
                            return '-'
                        }
                    }
                },{
                    "targets": [ 5 ],
                    "render":function(data, type, row){
                        return '<button class="btn btn-warning edit-todo" data-id="' + row.id + '">' +
                            '<i class="fa fa-edit fa-right"></i><span>Módosítás</span></button>' +
                            '<button class="btn btn-warning delete-todo" data-id="' + row.id + '">' +
                            '<i class="fa fa-trash fa-right"></i><span>Törlés</span></button>'
                    }
                },
            ],
            columns: [

                {
                    data: 'id'
                },
                {
                    data: 'name'
                },
                {
                    data: 'createdDate'
                },
                {
                    data: 'priority'
                },
                {
                    data: 'deadline'
                },
                {
                    data: 'id',
                    searchable: false,
                    orderable: false
                }
            ],
        });
        $todoTbl = $todoTbl;

    }

    $("#todo_filter").remove();

    $('input.todo_column_filter').on( 'keyup click', function () {
        todo_filterColumn( $(this).parents('th').attr('data-column') );
    } );
    function todo_filterColumn ( i ) {
        $('#table-todo').DataTable().column( i ).search(
            $('#todo_col'+i+'_filter').val()

        ).draw();
    }




    const baseSettings = (buttonApplyClass) => {

        return {
            timePicker: true,
            timePicker24Hour: true,
            showDropdowns: true,
            startDate: moment().startOf('hour'),
            endDate: moment().startOf('hour').add(32, 'hour'),
            applyButtonClasses: "btn-primary "+buttonApplyClass,
            locale: {
                format: 'YYYY-MM-DD HH:mm',
                "applyLabel": "Alkalmaz",
                "cancelLabel": "Mégsem",
                "fromLabel": "Mettől",
                "toLabel": "Meddig",
                "customRangeLabel": "Custom",
                "weekLabel": "W",
                "daysOfWeek": [
                    "Vas",
                    "Hé",
                    "Ke",
                    "Sz",
                    "Csü",
                    "Pé",
                    "Szo"
                ],
                "monthNames": [
                    "Jan.",
                    "Feb.",
                    "Márc.",
                    "Ápr.",
                    "Máj.",
                    "Jún.",
                    "Júl.",
                    "Aug.",
                    "Szep.",
                    "Okt.",
                    "Nov.",
                    "Dec."
                ],
                "firstDay": 1
            }}

    };
    $('#date-filter-todo-created').daterangepicker(baseSettings('btn-apply-todo-created'));
    $('#date-filter-todo-deadline').daterangepicker(baseSettings('btn-apply-todo-deadline'));

    $('.btn-apply-todo-created').on('click',function (event) {

        $('#table-todo').DataTable()
            .column(2)
            .search($('#date-filter-todo-created').val())
            .draw();
    });

    $('.btn-apply-todo-deadline').on('click',function (event) {

        $('#table-todo').DataTable()
            .column(4)
            .search($('#date-filter-todo-deadline').val())
            .draw();
    });

    $('#date-clear-todo-created').on('click',function (event) {

        $('#table-todo').DataTable()
            .column(2)
            .search('')
            .draw();
    });

    $('#date-clear-todo-deadline').on('click',function (event) {

        $('#table-todo').DataTable()
            .column(4)
            .search('')
            .draw();
    });

    $("#prioritySearch").on("change", function() {
        $("#prioritySearch").attr('data-column')
        console.log()

        if ($(this).val() == ""){
            $('table#table-todo').DataTable().column( 3 ).search(
                "",true,false
            ).draw();
        }else{
            $('table#table-todo').DataTable().column( 3 ).search(
                ""+$(this).val()+"",true,false
            ).draw()
        };
    });


    $("body").on("click", ".create-todo", function () {
        $.ajax({
            url: "/create-todo-dialog/",
            method: "GET",
            success: function (reply) {
                openTodoEditor(reply)
            }
        })

    });

    $("body").on("click", ".edit-todo", function () {
        var id = $(this).data("id");
        $.ajax({
            url: "/edit-todo-dialog/"+id,
            method: "GET",
            success: function (reply) {
                openTodoEditor(reply)
            }
        })

    });

    function openTodoEditor(reply) {
        var newDiv = $(document.createElement('div'));
        newDiv.html(reply);
        dialog = newDiv.dialog({
            height: 700,
            width: 750,
            modal: true,
            title: "Tárgy Szerkesztés",
            buttons: {
                Upload: function () {
                    var form = $('#todo-input-form')[0];
                    var data = new FormData(form);
                    var object = {};
                    data.forEach(function(value, key){
                        object[key] = value;
                    });
                    $.ajax({
                        type: "POST",
                        url: "/todo/api/createOrEditTodo",
                        data: JSON.stringify(object),
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        success: function (result) {
                            if (result.type === "success") {

                                removeDatatables();
                                let $todoTbl;
                                loadTodoDataTable($todoTbl);
                                dialog.dialog("close");
                            } else {
                                dialog.parent().effect("shake");
                                alert(result.msg);
                            }

                        }
                    })
                },
                Cancel: function () {
                    dialog.dialog("close");
                }
            },
            close: function () {
                $(this).remove();
            }
        });
    }



    $("body").on("click", ".delete-todo", function (e) {

        e.preventDefault();

        let id = $(this).data("id");
        if (confirm("Biztos, hogy törölni akarod?") === true) {
            $.ajax({
                type: "POST",
                url: "/todo/api/deleteTodo",
                data: JSON.stringify({"id": id}),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (response) {
                    removeDatatables();
                    let $todoTbl;
                    loadTodoDataTable($todoTbl);
                    $("#responseLine").hide();
                    $("#responseLine").removeClass( "alert-error alert-success" ).addClass('alert-'+ response.type);
                    $("#responseLine").show();

                    $("#responseMessage").text(response.msg);
                }
            });
        }

    });

    function  removeDatatables(){
        if ( $.fn.DataTable.isDataTable( '#table-todo' ) ) {
            $('#table-todo').DataTable().clear().destroy();
        }
    }
});