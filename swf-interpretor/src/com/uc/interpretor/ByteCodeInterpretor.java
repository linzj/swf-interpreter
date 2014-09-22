package com.uc.interpretor;

import com.uc.parser.ByteCode;

interface ByteCodeInterpretor {
	Object interpret (ByteCode code, Context contex);
}
