package be.vdab.mail.services;

import be.vdab.mail.domain.Lid;

import java.util.Optional;

public interface LidService {
    void registreer(Lid lid, String LedenURL);
    Optional<Lid> findById(long id);
}
