create table if not exists Account (id binary not null, balance double not null, createdAt timestamp, currencyCode integer not null, status integer not null, updatedAt timestamp, userId binary not null, primary key (id))
create table if not exists Transaction (id binary not null, amount double not null, createdAt timestamp, currencyCode integer not null, beneficiaryAccountId binary, remitterAccountId binary, primary key (id))
alter table Transaction add constraint if not exists BENEFICIARY_FOREIGN_REFERENCE foreign key (beneficiaryAccountId) references Account
alter table Transaction add constraint if not exists REMITTER_FOREIGN_REFERENCE foreign key (remitterAccountId) references Account
