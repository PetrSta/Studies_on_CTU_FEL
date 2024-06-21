#ifndef DATABASEQUERIES_QUERY_H
#define DATABASEQUERIES_QUERY_H

#include <vector>
#include <functional>
#include <atomic>

template<typename row_t>
using predicate_t = std::function<bool(const row_t &)>;



template<typename row_t>
bool is_satisfied_for_all(std::vector<predicate_t<row_t>> predicates, std::vector<row_t> data_table);

template<typename row_t>
bool is_satisfied_for_any(std::vector<predicate_t<row_t>> predicates, std::vector<row_t> data_table);


template<typename row_t>
bool is_satisfied_for_all(std::vector<predicate_t<row_t>> predicates, std::vector<row_t> data_table) {
    // Doimplementujte telo funkce, ktera rozhodne, zda pro VSECHNY dilci dotazy (obsazene ve
    // vektoru 'predicates') existuje alespon jeden zaznam v tabulce (reprezentovane vektorem
    // 'data_table'), pro ktery je dany predikat splneny.

    // Pro inspiraci si prostudujte kod, kterym muzete zjistit, zda prvni dilci dotaz plati,
    // tj., zda existuje alespon jeden radek v tabulce, pro ktery predikat reprezentovany
    // funkci predicates[i] plati:

    std::atomic<bool> query_result {true};

    // get predicate to test
    #pragma omp parallel for
    for(int i = 0; i < predicates.size(); i++) {
        auto current_predicate = predicates[i];
        std::atomic<bool> predicate_result {false};

        // get data to test predicate with
        for(int j = 0; j < data_table.size(); j++) {
            // if we find out that predicate is true we can end testing it
            if(current_predicate(data_table[j])) {
                predicate_result = true;
                break;
            }
        }

        // if we stopped testing predicate and result was false we dont need to test any more predicates
        if(!predicate_result) {
            query_result = false;
            #pragma omp cancel for
        }
    }

    return query_result;
}

template<typename row_t>
bool is_satisfied_for_any(std::vector<predicate_t<row_t>> predicates, std::vector<row_t> data_table) {
    // Doimplementujte telo funkce, ktera rozhodne, zda je ALESPON JEDEN dilci dotaz pravdivy.
    // To znamena, ze mate zjistit, zda existuje alespon jeden predikat 'p' a jeden zaznam
    // v tabulce 'r' takovy, ze p(r) vraci true.

    // Zamyslete se nad tim, pro ktery druh dotazu je vhodny jaky druh paralelizace. Vas
    // kod optimalizujte na situaci, kdy si myslite, ze navratova hodnota funkce bude true.
    // Je pro Vas dulezitejsi rychle najit splnujici radek pro jeden vybrany predikat, nebo
    // je dulezitejsi zkouset najit takovy radek pro vice predikatu paralelne?

    std::atomic<bool> query_result {false};

    // get date to test on
    #pragma omp parallel for
    for(int i = 0; i < data_table.size(); i++) {
        auto table_data = data_table[i];

        // get predicate to test
        for(int j = 0; j < predicates.size(); j++) {
            auto current_predicate = predicates[j];

            // if we find predicate that is true we can stop testing
            if(current_predicate(table_data)) {
                query_result = true;
                break;
            }
        }

        // if we stopped testing predicate and result was true we dont need to test any more predicates
        if(query_result) {
            #pragma omp cancel for
        }
    }

    /*
    // get data to test predicate with
    #pragma omp parallel for
    for(int i = 0; i < predicates.size(); i++) {
        auto current_predicate = predicates[i];

        // get predicate to test
        for(int j = 0; j < data_table.size(); j++) {
            auto table_data = data_table[j];

            // if we find predicate that is true we can stop testing
            if(current_predicate(table_data)) {
                query_result = true;
                break;
            }
        }

        // if we stopped testing predicate and result was true we dont need to test any more predicates
        if(query_result) {
            #pragma omp cancel for
        }
    }
    */

    return query_result;
}

#endif //DATABASEQUERIES_QUERY_H