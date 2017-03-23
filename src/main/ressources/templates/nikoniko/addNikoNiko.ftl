 <head>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

</head>

<body>
	<h1>${page}</h1>

    <form action = "${create_item}" method = "POST">
        <table class="table table-bordered table-hover">
            <#list sortedFields as field>
                <#if field != "id">
                    <#list item?keys as key>
                        <#if key == field>
                            <tr>
                                <th>${key}</th>
                                <td>
                                    <input type="text" name = "${key}">
                                </td>
                            </tr>
                        </#if>
                    </#list>
                </#if>
            </#list>
        </table>
        <input type="submit" value="create"><br>
    </form>
    <#if item["id"]??>
        <a href="${go_index}">Back</a><br>
    <#else>
        <a href="${go_index}">Back</a><br>
    </#if>
</body>