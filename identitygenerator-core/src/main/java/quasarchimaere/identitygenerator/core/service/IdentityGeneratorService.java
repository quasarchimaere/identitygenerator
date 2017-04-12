package quasarchimaere.identitygenerator.core.service;

import quasarchimaere.identitygenerator.core.model.Identity;

import java.io.UnsupportedEncodingException;

public interface IdentityGeneratorService {
    public Identity generateIdentity(int i) throws UnsupportedEncodingException;

    public Identity generateIdentity(String str) throws UnsupportedEncodingException;

    public Identity generateIdentity(byte[] bytes) throws UnsupportedEncodingException;
}
