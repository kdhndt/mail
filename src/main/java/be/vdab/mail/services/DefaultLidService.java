package be.vdab.mail.services;

import be.vdab.mail.domain.Lid;
import be.vdab.mail.mailing.LidMailing;
import be.vdab.mail.repositories.LidRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DefaultLidService implements LidService {
    private final LidRepository lidRepository;
    private final LidMailing lidMailing;

    public DefaultLidService(LidRepository lidRepository, LidMailing lidMailing) {
        this.lidRepository = lidRepository;
        this.lidMailing = lidMailing;
    }

    @Override
    public void registreer(Lid lid, String ledenURL) {
        lidRepository.save(lid);
        //todo: waarvoor wordt ledenURL gebruikt?
        lidMailing.stuurMailNaRegistratie(lid, ledenURL);
    }

    @Override
    public Optional<Lid> findById(long id) {
        return lidRepository.findById(id);
    }
}
