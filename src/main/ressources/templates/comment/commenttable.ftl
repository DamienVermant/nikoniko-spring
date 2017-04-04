<!DOCTYPE html>
<html>
<head>
    <!-- Encodage -->
    <meta charset="utf-8">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Redirection ???? -->
    <link rel="stylesheet" href="menu/">

  </head>

<style>
    <#include "employee.css">
</style>

<!-- HEAD -->
<div class="container-fluid">
    <div class="row-fluid">
        <div class="col-lg-2">
            <img class="logo" src="https://upload.wikimedia.org/wikipedia/fr/5/51/LOGO-CGI-1993-1998.svg">
        </div>
        <div class="col-lg-8">
            <div class="title">Niko-Niko</div>
        </div>
        <div class="col-lg-2">
            <div class="row-fluid">
                <div class="col-lg-12">
                    <div class="align">
                    <button onclick="location.href='/logout' " class="logout"> Déconnexion </button>
                    <button onclick="location.href='${back}'" class="vote"> Retour </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<table class="table table-bordered table-hover">
        <#list sortedFields as field>
            <#if field != "id">
                <#list item?keys as key>
                    <#if key == field>
                        <tr>
                            <th>${key}</th>
                            <#if item[key]?is_boolean>
                                <td>
                                    ${item[key]?c}
                                </td>
                            <#else>
                                <td>
                                    ${item[key]}
                                </td>
                            </#if>
                        </tr>
                    </#if>
                </#list>
            </#if>
        </#list>
    </table>



<!-- FOOTER -->
<div class="container-fluid">
    <div class="row-fluid">
        <div class="col-lg-12">
            <div class="copyright">&copy; Niko-Niko CGI 2017</div>
        </div>
    </div>
</div>
</html>
