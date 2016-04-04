package com.bko.viewresolver.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.bko.domain.DeploymentRequest;
import com.bko.domain.PatchMember;
import com.cw.scm.member.description.DescriptionManager;
import com.cw.scm.member.description.XmlMemberDescription;
import com.cw.scm.member.description.table.TableDescription;

public class DeploymentRequestMemberTypes {

	private DeploymentRequest deploymentRequest;
	// List<String> types;

	HashMap<String, List<String>> types = new HashMap<String, List<String>>();

	public DeploymentRequest getDeploymentRequest() {
		return deploymentRequest;
	}

	public void setDeploymentRequest(DeploymentRequest deploymentRequest) {
		this.deploymentRequest = deploymentRequest;
	}

	// public void setTypes(){
	// List<PatchMember> patchMemberList = deploymentRequest.getMemberList();
	// for (PatchMember patchMember : patchMemberList){
	// this.types.add(patchMember.getMemberType(), );
	// System.out.println("Member type: " + patchMember.getMemberType());
	// }
	// }

	public void generatePatchMemberTypes() {

		String fileName;
		File member_file;
		List<PatchMember> patchMemberList = deploymentRequest.getMemberList();
		XmlMemberDescription memberDbdesc;

		List<TableDescription> tableDescritption;

		// for (String type : this.types){
		String type = "";
		for (PatchMember pMember : patchMemberList) {
			type = pMember.getMemberType();
			fileName = "memberdescription/";
			System.out.println("Type: " + type);
			fileName = fileName + type + ".xml";
			member_file = new File(fileName);
			memberDbdesc = DescriptionManager.getInstance().getDescriptionFromXml(type, member_file);

			List<String> tables = new ArrayList<String>();
			if (memberDbdesc != null) {
				tableDescritption = memberDbdesc.getTableDescriptions();
				for (TableDescription tableDesc : tableDescritption) {
					System.out.println("Table: " + tableDesc.name);
					tables.add(tableDesc.name);
					this.types.put(type, tables);
				}
			}
		}
	}

	public HashMap<String, List<String>> getTypes() {
		return types;
	}

	public void setTypes(HashMap<String, List<String>> types) {
		this.types = types;
	}

	public void main(String[] args) {

		// MemberDescription desc =
		// DescriptionManager.getInstance().getSimpleDescription(memberType,
		// null);
		XmlMemberDescription memberDbdesc;
		List<TableDescription> des;

		// DescriptionManager.getInstance();
		List<String> types = DescriptionManager.getMemberTypeList();
		String fileName;
		File member_file;
		// List<TableDescription> des;

		for (String type : types) {

			fileName = "memberdescription/";
			System.out.println("Type: " + type);
			fileName = fileName + type + ".xml";
			member_file = new File(fileName);
			memberDbdesc = DescriptionManager.getInstance().getDescriptionFromXml(type, member_file);
			if (memberDbdesc != null) {
				des = memberDbdesc.getTableDescriptions();
				for (TableDescription tableDesc : des) {
					System.out.println("Table: " + tableDesc.name);
				}
			}
		}
	}
}
