package com.bko.service;

import java.util.List;

import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.domain.TransferOperation;



public interface PatchService {
	List<PatchMember> getPatchMember( String refpat );
	List<TransferOperation> getTransferOperation(String refpat);
	List<Patch> getPatchDescription(String refpat);
}
