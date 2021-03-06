# 2FAce-verification

Predmetni projekat iz predmeta Osnovi računarske inteligencije (ORI) - 2FA sistem zasnovan na neuronskim mrežama za detekciju i verifikaciju lica.

## Članovi tima

 * Marko Njegomir
 * Dušan Erdeljan

## Sadržaj

 * [Poster](#poster)
 * [Izgled aplikacije](#izgled-aplikacije)
 * [Pokretanje projekta](#pokretanje-projekta)
   * [Preduslovi](#preduslovi)
   * [Pokretanje](#pokretanje)
     * [Frontend](#frontend)
     * [Eureka server](#eureka-server)
     * [Bekend](#bekend)
     * [Servis(i) za verifikaciju lica](#servis-za-verifikaciju-lica)
     * [Tensorboard](#tensorboard)

## Poster

<p align="center">
  <img src="/docs/Poster.png">
  <p align="center">Ilustracija 1 - Poster projekta.</p>
</p>

## Izgled aplikacije

<p align="center">
  <img src="/docs/images/login.jpg">
  <p align="center">Ilustracija 2 - Stranica za prijavljivanje.</p>
</p>

<p align="center">
  <img src="/docs/images/register.jpg">
  <p align="center">Ilustracija 3 - Stranica za registraciju.</p>
</p>


## Pokretanje projekta

### Preduslovi

Za pokretanje projekta potrebno je instalirati `Pg Admin` (https://www.pgadmin.org/) i `MongoDB Server` (https://www.mongodb.com/try/download/community). Zatim je potrebno napraviti Postgre bazu sa nazivom `2FAce`. `Python` verzija na kojoj je projekat testiran je `3.8`.

### Pokretanje

Potrebno je pokrenuti sve projektu u navedenom redosledu (bitno je pokrenuti eureka server pre bekend aplikacije i servisa za verifikaciju lica).

#### Frontend

Iz `2face-frontend` foldera potrebno je pokrenuti komandu `npm ci` (samo pre prvog pokretanja), a zatim pokrenuti komandu `npm run serve`. Frontend aplikacija će biti startovana na adresi `http://localhost:8080/`.

#### Eureka server

Eureka server je moguće pokrenuti iz nekog razvojnog okruženja ili pokretanjem komande `.\mvnw spring-boot:run` iz foldera `eureka-server`. Ovim je eureka server pokrenut i registrovani servisi se mogu videti na adresi `http://localhost:8761/`.

#### Bekend

Bekend aplikaciju je moguće pokrenuti iz nekog razvojnog okruženja ili pokretanjem komande `.\mvnw spring-boot:run` iz foldera `2face-backend`.

#### Servis za verifikaciju lica

Pre pokretanja servisa za verifikaciju lica potrebno je instalirati sve biblioteke iz fajla `face-verification-service/requirements.txt`. Poželjno je pre instalacije podesiti virtuelno okruženje. Servise za verifikaciju lica je moguće pokrenuti iz nekog razvojnog okruženja, čime će im biti dodeljen podrazumevani port `8181`. Dodatno, servise je moguće pokrenuti pokretanjem komande `python ./flask_verification_service.py --port <PORT>` iz foldera `face-verification-service/src`. Ovim će servis biti pokrenut na prosleđenom portu.

#### Tensorboard

Za pokretanje tensorboard aplikacije koja prikazuje rezultate prikupljene prilikom treniranja modela za verifikaciju potrebno je pozicionirati se u folder `face-verification-service/src` i pokrenuti komandu 
`tensorboard --logdir logs/fit`. Tensorboard je nakon toga moguće koristiti na adresi `http://localhost:6006/`.
