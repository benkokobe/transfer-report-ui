package com.bko.service;

import java.util.List;

import com.bko.domain.DeploymentRequestTransferOperation;
import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.domain.TransferOperation;



public interface DeploymentRequestService {
	public int getNumberOfPatches(String deploymentRequestName);
	public int getnumberOfTransferOperations(String deploymentRequestName);
	public int getNumberOfManualTransferOperations(String deploymentRequestName);
	public int getNumberOfSubjects(String deploymentRequestName);
	public List<Patch> getPatchList(String deploymentRequest);
	//public List<Patch> getPatchListComplete( String NAMLOT );
	public List<PatchMember> getDRMembers(String drName);
	public List<DeploymentRequestTransferOperation> getTransferOperation(String deploymentRequest);
	public String getRefLot(String drName);
	public List<TransferOperation> getMissingYe(String reflot);
	public String getEnvDst(String deploymentRequestName);
	public String getEnvSrc(String deploymentRequestName);
	public String getSynopsis(String deploymentRequestName);

}
