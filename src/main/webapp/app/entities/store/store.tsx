import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './store.reducer';

export const Store = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const storeList = useAppSelector(state => state.store.entities);
  const loading = useAppSelector(state => state.store.loading);
  const totalItems = useAppSelector(state => state.store.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="store-heading" data-cy="StoreHeading">
        <Translate contentKey="dealNdiscountApp.store.home.title">Stores</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dealNdiscountApp.store.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/store/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dealNdiscountApp.store.home.createLabel">Create new Store</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {storeList && storeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dealNdiscountApp.store.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('storeName')}>
                  <Translate contentKey="dealNdiscountApp.store.storeName">Store Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storeName')} />
                </th>
                <th className="hand" onClick={sort('storeType')}>
                  <Translate contentKey="dealNdiscountApp.store.storeType">Store Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storeType')} />
                </th>
                <th className="hand" onClick={sort('storeCategory')}>
                  <Translate contentKey="dealNdiscountApp.store.storeCategory">Store Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storeCategory')} />
                </th>
                <th className="hand" onClick={sort('address')}>
                  <Translate contentKey="dealNdiscountApp.store.address">Address</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('address')} />
                </th>
                <th className="hand" onClick={sort('phone')}>
                  <Translate contentKey="dealNdiscountApp.store.phone">Phone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phone')} />
                </th>
                <th className="hand" onClick={sort('geoLocation')}>
                  <Translate contentKey="dealNdiscountApp.store.geoLocation">Geo Location</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('geoLocation')} />
                </th>
                <th>
                  <Translate contentKey="dealNdiscountApp.store.city">City</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {storeList.map((store, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/store/${store.id}`} color="link" size="sm">
                      {store.id}
                    </Button>
                  </td>
                  <td>{store.storeName}</td>
                  <td>
                    <Translate contentKey={`dealNdiscountApp.StoreType.${store.storeType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`dealNdiscountApp.StoreCategory.${store.storeCategory}`} />
                  </td>
                  <td>{store.address}</td>
                  <td>{store.phone}</td>
                  <td>{store.geoLocation}</td>
                  <td>{store.city ? <Link to={`/city/${store.city.id}`}>{store.city.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/store/${store.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/store/${store.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/store/${store.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="dealNdiscountApp.store.home.notFound">No Stores found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={storeList && storeList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Store;
