import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './store.reducer';

export const StoreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const storeEntity = useAppSelector(state => state.store.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storeDetailsHeading">
          <Translate contentKey="dealNdiscountApp.store.detail.title">Store</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{storeEntity.id}</dd>
          <dt>
            <span id="storeName">
              <Translate contentKey="dealNdiscountApp.store.storeName">Store Name</Translate>
            </span>
          </dt>
          <dd>{storeEntity.storeName}</dd>
          <dt>
            <span id="storeType">
              <Translate contentKey="dealNdiscountApp.store.storeType">Store Type</Translate>
            </span>
          </dt>
          <dd>{storeEntity.storeType}</dd>
          <dt>
            <span id="storeCategory">
              <Translate contentKey="dealNdiscountApp.store.storeCategory">Store Category</Translate>
            </span>
          </dt>
          <dd>{storeEntity.storeCategory}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="dealNdiscountApp.store.address">Address</Translate>
            </span>
          </dt>
          <dd>{storeEntity.address}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="dealNdiscountApp.store.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{storeEntity.phone}</dd>
          <dt>
            <span id="geoLocation">
              <Translate contentKey="dealNdiscountApp.store.geoLocation">Geo Location</Translate>
            </span>
          </dt>
          <dd>{storeEntity.geoLocation}</dd>
          <dt>
            <Translate contentKey="dealNdiscountApp.store.city">City</Translate>
          </dt>
          <dd>{storeEntity.city ? storeEntity.city.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/store" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/store/${storeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StoreDetail;
