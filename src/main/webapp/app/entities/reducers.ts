import bank from 'app/entities/bank/bank.reducer';
import card from 'app/entities/card/card.reducer';
import store from 'app/entities/store/store.reducer';
import deal from 'app/entities/deal/deal.reducer';
import city from 'app/entities/city/city.reducer';
import favorite from 'app/entities/favorite/favorite.reducer';
import extendedUser from 'app/entities/extended-user/extended-user.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  bank,
  card,
  store,
  deal,
  city,
  favorite,
  extendedUser,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
