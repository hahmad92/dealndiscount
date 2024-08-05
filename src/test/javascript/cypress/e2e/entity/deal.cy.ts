import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Deal e2e test', () => {
  const dealPageUrl = '/deal';
  const dealPageUrlPattern = new RegExp('/deal(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const dealSample = { discountPercentage: 84, isDealActive: true };

  let deal;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/deals+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/deals').as('postEntityRequest');
    cy.intercept('DELETE', '/api/deals/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (deal) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/deals/${deal.id}`,
      }).then(() => {
        deal = undefined;
      });
    }
  });

  it('Deals menu should load Deals page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('deal');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Deal').should('exist');
    cy.url().should('match', dealPageUrlPattern);
  });

  describe('Deal page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dealPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Deal page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/deal/new$'));
        cy.getEntityCreateUpdateHeading('Deal');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dealPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/deals',
          body: dealSample,
        }).then(({ body }) => {
          deal = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/deals+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/deals?page=0&size=20>; rel="last",<http://localhost/api/deals?page=0&size=20>; rel="first"',
              },
              body: [deal],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(dealPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Deal page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('deal');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dealPageUrlPattern);
      });

      it('edit button click should load edit Deal page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Deal');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dealPageUrlPattern);
      });

      it('edit button click should load edit Deal page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Deal');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dealPageUrlPattern);
      });

      it('last delete button click should delete instance of Deal', () => {
        cy.intercept('GET', '/api/deals/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('deal').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', dealPageUrlPattern);

        deal = undefined;
      });
    });
  });

  describe('new Deal page', () => {
    beforeEach(() => {
      cy.visit(`${dealPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Deal');
    });

    it('should create an instance of Deal', () => {
      cy.get(`[data-cy="discountPercentage"]`).type('47');
      cy.get(`[data-cy="discountPercentage"]`).should('have.value', '47');

      cy.get(`[data-cy="description"]`).type('woot right unpleasant');
      cy.get(`[data-cy="description"]`).should('have.value', 'woot right unpleasant');

      cy.get(`[data-cy="startDate"]`).type('2024-08-04T22:15');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2024-08-04T22:15');

      cy.get(`[data-cy="endDate"]`).type('2024-08-05T02:51');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2024-08-05T02:51');

      cy.get(`[data-cy="isDealActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isDealActive"]`).click();
      cy.get(`[data-cy="isDealActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        deal = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', dealPageUrlPattern);
    });
  });
});
