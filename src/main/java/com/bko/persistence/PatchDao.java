package com.bko.persistence;

import java.util.List;

import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.domain.TransferOperation;



public interface PatchDao {
	public Patch getPatch(String patchId);
	public List<TransferOperation> getTransferOperation(String patchId);
	public List<PatchMember> getPatchMembers(String patchId);
	public List<Patch> getPatchDescription(String NAMLOT);

}
