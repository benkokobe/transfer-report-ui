<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>RIC release report Generator</title>
	<!-- Bootstrap -->
	<link href="/public/lib/bootstrap-3.1.1/css/bootstrap.min.css" rel="stylesheet">

	<!-- Our CSS -->
	<link href="/public/css/styles.css" rel="stylesheet">
	
	<!-- Custom CSS -->
    <!--  <link href="/public/css/simple-sidebar.css" rel="stylesheet"> -->
    <link href="/public/css/small-business.css" rel="stylesheet">

</head>
<body>
   
   <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
             
                <ul class="nav navbar-nav navbar-left">
                    <li>
                        <a href="/">DR Generator (DB)</a>
                    </li>
                    <li>
                        <a href="/formatter">Platform Env. Advanced report Formatter</a>
                    </li>
                    <li>
                        <a href="/release/">Release Report Generator (Synergy)</a>
                    </li>
                </ul>
                
              
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="/about">About</a>
                        <mark>Connected to:${env_name}</mark>
                    </li>
                </ul>               
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>
    
    <!-- Page Content -->
<div class="container">
	<div class="jumbotron" ">
	
	<c:if test="${not empty flashMessage}">
		<div class="alert alert-${flashKind} alert-dismissable">
		  <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
		  <h3 class="bg-success"><em>${flashMessage}</em></h3>
		</div>
	</c:if>