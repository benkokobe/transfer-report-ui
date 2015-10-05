<%@include file="includes/header.jsp"%>

<!-- Page Content -->
<div id="page-content-wrapper">
	<div class="container-fluid">
		<div class="row">
			<div class="col-lg-12">
				<h1>Deployment request generator</h1>

				<p><spring:message code="message" /></p>

			</div>
		</div>
	</div>
	<form:form method="post" modelAttribute="deploymentRequest">
		<table>
			<tr>
				<td>Deployment Request Name:</td>
				<td><form:input path="drName" /></td>
				<td><form:errors path="drName" cssClass="error" /></td>
			</tr>

			<tr>
				<td colspan="3"><input type="hidden" value="0" name="_page" />
					<input type="submit" value="Next" name="_target1" /> <input
					type="submit" value="Cancel" name="_cancel" /></td>
			</tr>
		</table>
	</form:form>
</div>
<!-- /#page-content-wrapper -->



<%@include file="includes/footer.jsp"%>