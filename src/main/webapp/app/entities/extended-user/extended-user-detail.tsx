import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './extended-user.reducer';

export const ExtendedUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const extendedUserEntity = useAppSelector(state => state.extendedUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="extendedUserDetailsHeading">
          <Translate contentKey="dealNdiscountApp.extendedUser.detail.title">ExtendedUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{extendedUserEntity.id}</dd>
          <dt>
            <span id="dob">
              <Translate contentKey="dealNdiscountApp.extendedUser.dob">Dob</Translate>
            </span>
          </dt>
          <dd>{extendedUserEntity.dob ? <TextFormat value={extendedUserEntity.dob} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="dealNdiscountApp.extendedUser.city">City</Translate>
          </dt>
          <dd>{extendedUserEntity.city ? extendedUserEntity.city.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/extended-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/extended-user/${extendedUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExtendedUserDetail;
