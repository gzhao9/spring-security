package org.springframework.security.acls.afterinvocation;

import org.springframework.security.acls.model.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

public class MockAclService {
	static AclService mockservice(Acl acl){
		AclService service=mock(AclService.class);
		given(service.readAclById(any(), any())).willReturn(acl);
		return service;
	}

}
