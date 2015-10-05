<%@include file="includes/header.jsp"%>


<form:form  method="post" modelAttribute="user">
<div class="form-group">
    <label for="conflictFile">Enter advanced report generated csv file (saved in your home directory):</label>
    <form:input path="conflictFile" class="form-control" placeholder="Enter name of csv file here"/>
</div>

<div class="form-group">
  <button type="submit" class="btn btn-default">Submit</button>
  </div>
</form:form>
<%@include file="includes/footer.jsp"%>