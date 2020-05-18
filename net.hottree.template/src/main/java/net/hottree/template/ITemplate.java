package net.hottree.template;

import java.util.List;

import net.hottree.template.impl.TemplateKeyNotFoundException;

/**
 * Interfejs szablonów w HotTree. Zawiera on metody i klucze do operacji na ich
 * treœci. Operacje nie s¹ zsynchronizowane.
 * <p>
 * <b>Note:</b> Metody w których zamiast nazwy podaje siê indeks klucza s¹
 * szybsze od tych gdzie podaje siê nazwê klucza. Indeksy kluczy maj¹ wartoœæ od
 * 0 do n - 1, gdzie n to liczba kluczy. Ponadto indeksy kluczy odnosz¹ siê do
 * aktualnej liczby kluczy.
 */
public interface ITemplate extends IOutput, IDisposable {

	/**
	 * Zwraca wzglêdn¹ œcie¿kê do pliku szablonu.
	 * 
	 * @return wzglêdna œcie¿ka do pliku szablonu
	 */
	public String getLocation();

	/**
	 * Zwraca nazwê szablonu.
	 * 
	 * @return nazwê szablonu; je¿eli nie bêdzie to wycinek szablonu to zwróci
	 *         wzglêdn¹ œcie¿kê do pliku szablonu
	 */
	public String getName();

	/**
	 * Dodaje do koñca szablonu obiekt podany przez parametr <tt>object</tt>. Je¿eli
	 * nie jest on typu <tt>IOutput</tt> to zostanie on sprowadzony do tekstu przez
	 * metodê <tt>Object.toString()</tt>.
	 * 
	 * @param object obiekt, który ma byæ dodany do koñca szablonu
	 */
	public void append(Object object);

	/**
	 * Zastêpuje wszystkie klucze o podanej przez parametr <tt>keyName</tt> nazwie
	 * obiektem przekazanym przez parametr <tt>object</tt>. Je¿eli podany obiekt nie
	 * jest typu <tt>IOutput</tt> to zostanie on sprowadzony do tekstu przez jego
	 * metodê <tt>Object.toString()</tt>.
	 * 
	 * @param keyName nazwa klucza(y), który ma zostaæ zast¹pionym obiektem
	 * @param object  obiekt, który ma byæ wstawiony w miejsce klucza(y)
	 * @throws TemplateKeyNotFoundException je¿eli klucz o podanej nazwie nie
	 *                                      istnieje
	 */
	public void replace(String keyName, Object object) throws TemplateKeyNotFoundException;

	/**
	 * Zastêpuje klucz o podanym przez parametr <tt>keyIndex</tt> indeksie obiektem
	 * podanym przez parametr <tt>object</tt>. Je¿eli podany obiekt nie jest typu
	 * <tt>IOutput</tt> to zostanie on sprowadzony do tekstu przez metodê
	 * <tt>Object.toString()</tt>.
	 * 
	 * @param keyIndex indeks klucza, który ma byæ zast¹piony bêd¹cy liczb¹ od 0 do
	 *                 n-1, gdzie n to liczba kluczy w szablonie
	 * @param object   obiekt, który ma byæ wstawiony w miejsce klucza
	 * @throws TemplateKeyNotFoundException je¿eli klucz o podanym indeksie nie
	 *                                      istnieje
	 */
	public void replace(int keyIndex, Object object) throws TemplateKeyNotFoundException;

	/**
	 * Zastêpuje wszystkie fragmenty szablonu zawarte pomiêdzy kluczam o nazwie
	 * <tt>keyNameFrom</tt>, a kluczam o nazwie <tt>keyNameTo</tt> obiektem podanym
	 * przez parametr <tt>object</tt>. Je¿eli podany obiekt nie jest typu
	 * <tt>IOutput</tt> to zostanie on sprowadzony do tekstu przez metodê
	 * <tt>Object.toString()</tt>.
	 * <p>
	 * Je¿eli parametr <tt>keyNameFrom</tt> bêdzie mia³ wartoœæ <tt>null</tt>
	 * wówczas bêdzie on traktowany jako pocz¹tek szablonu. Je¿eli zaœ parametr
	 * <tt>keyNameTo</tt> bêdzie mia³ wartoœæ <tt>null</tt> wówczas bêdzie on
	 * wskazywa³ na koniec szablonu. Przyk³adowo je¿eli chcielibyœmy zast¹piæ
	 * szablon od jego pocz¹tku do klucza "KEY_A" (³¹cznie) pustym tekstem wówczas
	 * wywo³ujemy metodê <tt>tamplate.replace(null, "KEY_A", "");</tt>.
	 * <p>
	 * <b>Note:</b> Fragment szablonu jest zastêpowany ³¹cznie ze wskazanymi
	 * kluczami granicznymi.
	 * 
	 * @param keyFromName nazwa klucza okreœlaj¹cego pocz¹tek fragmentu szablonu,
	 *                    który ma zostaæ zast¹piony lub <tt>null</tt> je¿eli
	 *                    szablon ma byæ zast¹piony od jego pocz¹tku
	 * @param keyToName   nazwa klucza okreœlaj¹cego koniec fragmentu szablonu,
	 *                    który ma zostaæ zast¹piony lub <tt>null</tt> je¿eli
	 *                    szablon ma byæ zast¹piony do jego koñca
	 * @param object      obiekt, który ma byæ wstawiony w miejsce szablonu
	 *                    okreœlonego przez parametry <tt>keyNameFrom</tt> i
	 *                    <tt>keyNameTo</tt>
	 * @throws TemplateKeyNotFoundException je¿eli przynajmniej jeden z kluczy o
	 *                                      podanej nazwie nie istnieje
	 */
	public void replace(String keyFromName, String keyToName, Object object) throws TemplateKeyNotFoundException;

	/**
	 * Zastêpuje fragment szablonu pomiêdzy kluczem o indeksie
	 * <tt>keyIndexFrom</tt>, a kluczem o indeksie <tt>keyIndexTo</tt> obiektem
	 * podanym przez paramtr <tt>object</tt>. Je¿eli podany obiekt nie jest typu
	 * <tt>IOutput</tt> to zostanie on sprowadzony do tekstu przez metodê
	 * <tt>Object.toString()</tt>.
	 * <p>
	 * Je¿eli parametr <tt>keyIndexFrom</tt> bêdzie mia³ wartoœæ -1 wówczas bêdzie
	 * on traktowany jako pocz¹tek szablonu. Je¿eli zaœ parametr <tt>keyIndexTo</tt>
	 * bêdzie mia³ wartoœæ -1 wówczas bêdzie on wskazywa³ na koniec szablonu.
	 * Przyk³adowo je¿eli chcielibyœmy zast¹piæ szablon od jego pocz¹tku do klucza o
	 * indeksie 2 (³¹cznie) pustym tekstem wówczas wywo³ujemy metodê
	 * <tt>tamplate.replace(-1, 2, "");</tt>.
	 * <p>
	 * <b>Note:</b> Fragment szablonu jest zastêpowany ³¹cznie ze wskazanymi
	 * kluczami granicznymi. Indeksy kluczy s¹ naliczane od 0 do n - 1; gdzie n to
	 * liczba dostêpnych kluczy w szablonie.
	 * 
	 * @param keyFromIndex indeks klucza okreœlaj¹cego pocz¹tek fragmentu szablonu,
	 *                     który ma zostaæ zast¹piony bêd¹cy liczb¹ od 0 do n-1,
	 *                     gdzie n to liczba kluczy w szablonie, lub -1 je¿eli
	 *                     szablon ma byæ zast¹piony od jego pocz¹tku
	 * @param keyToIndex   indeks klucza okreœlaj¹cego koniec fragmentu szablonu,
	 *                     który ma zostaæ zast¹piony bêd¹cy liczb¹ od 0 do n-1,
	 *                     gdzie n to liczba kluczy w szablonie lub -1 je¿eli
	 *                     szablon ma byæ zast¹piony do jego koñca
	 * @param object       obiekt, który ma byæ wstawiony w miejsce szablonu
	 *                     okreœlonego przez parametry <tt>keyIndexFrom</tt> i
	 *                     <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je¿eli przynajmniej jeden z kluczy o
	 *                                      podanym indeksie nie istnieje
	 */
	public void replace(int keyFromIndex, int keyToIndex, Object object) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca szablon bêd¹cy kopi¹ szablonu pomiêdzy kluczem o nazwie
	 * <tt>keyNameFrom</tt> , a kluczem o nazwie <tt>keyNameTo</tt>.
	 * <p>
	 * Je¿eli parametr <tt>keyNameFrom</tt> bêdzie mia³ wartoœæ <tt>null</tt>
	 * wówczas bêdzie on traktowany jako pocz¹tek szablonu. Je¿eli zaœ parametr
	 * <tt>keyNameTo</tt> bêdzie mia³ wartoœæ <tt>null</tt> wówczas bêdzie on
	 * wskazywa³ na koniec szablonu. Przyk³adowo je¿eli chcielibyœmy skopiowaæ
	 * szablon od jego pocz¹tku do klucza "KEY_A" wówczas wywo³ujemy metodê
	 * <tt>tamplate.copy(null, "KEY_A");</tt>.
	 * 
	 * @param keyFromName nazwa klucza okreœlaj¹cego pocz¹tek kopi szablonu, która
	 *                    ma zostaæ zwrócona lub <tt>null</tt> je¿eli ma byæ to
	 *                    kopia od pocz¹tku szablonu
	 * @param keyToName   nazwa klucza okreœlaj¹cego koniec kopi szablonu, która ma
	 *                    zostaæ zwrócona lub <tt>null</tt> je¿eli ma byæ to kopia
	 *                    do koñca szablonu
	 * @return kopia szablonu okreœlona przez parametry <tt>keyIndexFrom</tt> i
	 *         <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je¿eli przynajmniej jeden z kluczy o
	 *                                      podanej nazwie nie istnieje
	 */
	public ITemplate copy(String keyFromName, String keyToName) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca szablon bêd¹cy kopi¹ szablonu pomiêdzy kluczem o indeksie
	 * <tt>keyIndexFrom</tt>, a kluczem o indeksie <tt>keyIndexTo</tt>.
	 * <p>
	 * Je¿eli parametr <tt>keyIndexFrom</tt> bêdzie mia³ wartoœæ -1 wówczas bêdzie
	 * on traktowany jako pocz¹tek szablonu. Je¿eli zaœ parametr <tt>keyIndexTo</tt>
	 * bêdzie mia³ wartoœæ -1 wówczas bêdzie on wskazywa³ na koniec szablonu.
	 * Przyk³adowo je¿eli chcielibyœmy skopiowaæ szablon od jego pocz¹tku do klucza
	 * o indeksie 2 wówczas wywo³ujemy metodê <tt>tamplate.copy(-1, 2);</tt>.
	 * <p>
	 * <b>Note:</b> Indeksy kluczy s¹ naliczane od 0 do n - 1; gdzie n to liczba
	 * dostêpnych kluczy w szablonie.
	 * 
	 * @param keyFromIndex indeks klucza okreœlaj¹cego pocz¹tek kopi szablonu, która
	 *                     ma zostaæ zwrócona bêd¹cy liczb¹ od 0 do n-1, gdzie n to
	 *                     liczba kluczy w szablonie lub -1 je¿eli ma byæ to kopia
	 *                     od pocz¹tku szablonu
	 * @param keyToIndex   indeks klucza okreœlaj¹cego koniec kopi szablonu, która
	 *                     ma zostaæ zwrócona bêd¹cy liczb¹ od 0 do n-1, gdzie n to
	 *                     liczba kluczy w szablonie lub -1 je¿eli ma byæ to kopia
	 *                     do koñca szablonu
	 * @return kopia szablonu okreœlona przez parametry <tt>keyIndexFrom</tt> i
	 *         <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je¿eli przynajmniej jeden z kluczy o
	 *                                      podanym indeksie nie istnieje
	 */
	public ITemplate copy(int keyFromIndex, int keyToIndex) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca tekst szablonu pomiêdzy kluczem o nazwie <tt>keyNameFrom</tt>, a
	 * kluczem o nazwie <tt>keyNameTo</tt>.
	 * <p>
	 * Je¿eli parametr <tt>keyNameFrom</tt> bêdzie mia³ wartoœæ <tt>null</tt>
	 * wówczas bêdzie on traktowany jako pocz¹tek szablonu. Je¿eli zaœ parametr
	 * <tt>keyNameTo</tt> bêdzie mia³ wartoœæ <tt>null</tt> wówczas bêdzie on
	 * wskazywa³ na koniec szablonu. Przyk³adowo je¿eli chcielibyœmy zwróciæ tekst
	 * od pocz¹tku szablonu do klucza "KEY_A" wówczas wywo³ujemy metodê
	 * <tt>tamplate.copyText(null, "KEY_A");</tt>.
	 * 
	 * @param keyFromName nazwa klucza okreœlaj¹cego pocz¹tek tekstu, który ma
	 *                    zostaæ zwrócony lub <tt>null</tt> je¿eli ma byæ to tekst
	 *                    od pocz¹tku szablonu
	 * @param keyToName   nazwa klucza okreœlaj¹cego koniec tekstu, który ma zostaæ
	 *                    zwrócony lub <tt>null</tt> je¿eli ma byæ to tekst do koñca
	 *                    szablonu
	 * @return tekst szablonu pomiêdzy kluczami o nazwie <tt>keyNameFrom</tt>, i
	 *         <tt>keyNameTo</tt>
	 * @throws TemplateKeyNotFoundException je¿eli przynajmniej jeden z kluczy o
	 *                                      podanej nazwie nie istnieje
	 */
	public String copyText(String keyFromName, String keyToName) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca tekst szablon pomiêdzy kluczem o indeksie <tt>keyIndexFrom</tt>, a
	 * kluczem o indeksie <tt>keyIndexTo</tt>.
	 * <p>
	 * Je¿eli parametr <tt>keyIndexFrom</tt> bêdzie mia³ wartoœæ -1 wówczas bêdzie
	 * on traktowany jako pocz¹tek szablonu. Je¿eli zaœ parametr <tt>keyIndexTo</tt>
	 * bêdzie mia³ wartoœæ -1 wówczas bêdzie on wskazywa³ na koniec szablonu.
	 * Przyk³adowo je¿eli chcielibyœmy zwróciæ tekst od pocz¹tku szablonu do klucza
	 * o indeksie 2 wówczas wywo³ujemy metodê <tt>tamplate.copyText(-1, 2);</tt>.
	 * <p>
	 * <b>Note:</b> Indeksy kluczy s¹ naliczane od 0 do n - 1; gdzie n to liczba
	 * dostêpnych kluczy w szablonie.
	 * 
	 * @param keyFromIndex indeks klucza okreœlaj¹cego pocz¹tek tekstu, który ma
	 *                     zostaæ zwrócony bêd¹cy liczb¹ od 0 do n-1, gdzie n to
	 *                     liczba kluczy w szablonie lub -1 je¿eli ma byæ to tekst
	 *                     od pocz¹tku szablonu
	 * @param keyToIndex   indeks klucza okreœlaj¹cego koniec tekstu, który ma
	 *                     zostaæ zwrócony bêd¹cy liczb¹ od 0 do n-1, gdzie n to
	 *                     liczba kluczy w szablonie lub -1 je¿eli ma byæ to tekst
	 *                     do koñca szablonu
	 * @return tekst szablonu pomiêdzy kluczami o indeksie <tt>keyIndexFrom</tt> , i
	 *         <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je¿eli przynajmniej jeden z kluczy o
	 *                                      podanym indeksie nie istnieje
	 */
	public String copyText(int keyFromIndex, int keyToIndex) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca wycinek szablonu pomiêdzy kluczem o nazwie <tt>keyNameFrom</tt>, a
	 * kluczem o nazwie <tt>keyNameTo</tt>.
	 * <p>
	 * Je¿eli parametr <tt>keyNameFrom</tt> bêdzie mia³ wartoœæ <tt>null</tt>
	 * wówczas bêdzie on traktowany jako pocz¹tek szablonu. Je¿eli zaœ parametr
	 * <tt>keyNameTo</tt> bêdzie mia³ wartoœæ <tt>null</tt> wówczas bêdzie on
	 * wskazywa³ na koniec szablonu. Przyk³adowo je¿eli chcielibyœmy zwróciæ wycinek
	 * szablonu od jego pocz¹tku do klucza "KEY_A" wówczas wywo³ujemy metodê
	 * <tt>tamplate.cut(null, "KEY_A");</tt>.
	 * 
	 * 
	 * @param keyFromName nazwa klucza okreœlaj¹cego pocz¹tek wycinka szablonu,
	 *                    który ma zostaæ zwrócony lub <tt>null</tt> je¿eli ma byæ
	 *                    to wycinek od pocz¹tku szablonu
	 * @param keyToName   nazwa klucza okreœlaj¹cego koniec wycinka szablonu, który
	 *                    ma zostaæ zwrócony lub <tt>null</tt> je¿eli ma byæ to
	 *                    wycinek do koñca szablonu
	 * @return wycinek szablonu pomiêdzy kluczami o nazwie <tt>keyNameFrom</tt> i
	 *         <tt>keyNameTo</tt>
	 * @throws TemplateKeyNotFoundException je¿eli przynajmniej jeden z kluczy o
	 *                                      podanej nazwie nie istnieje
	 */
	public ITemplate cut(String keyFromName, String keyToName) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca wycinek szablonu pomiêdzy kluczem o indeksie <tt>keyIndexFrom</tt> , a
	 * kluczem o indeksie <tt>keyIndexTo</tt>.
	 * <p>
	 * Je¿eli parametr <tt>keyIndexFrom</tt> bêdzie mia³ wartoœæ -1 wówczas bêdzie
	 * on traktowany jako pocz¹tek szablonu. Je¿eli zaœ parametr <tt>keyIndexTo</tt>
	 * bêdzie mia³ wartoœæ -1 wówczas bêdzie on wskazywa³ na koniec szablonu.
	 * Przyk³adowo je¿eli chcielibyœmy zwróciæ wycinek szablonu od jego pocz¹tku do
	 * klucza o indeksie 2 wówczas wywo³ujemy metodê <tt>tamplate.cut(-1, 2);</tt>.
	 * <p>
	 * <b>Note:</b> Indeksy kluczy s¹ naliczane od 0 do n - 1; gdzie n to liczba
	 * dostêpnych kluczy w szablonie.
	 * 
	 * @param keyFromIndex indeks klucza okreœlaj¹cego pocz¹tek wycinka szablonu,
	 *                     który ma zostaæ zwrócony bêd¹cy liczb¹ od 0 do n-1, gdzie
	 *                     n to liczba kluczy w szablonie lub -1 je¿eli ma byæ to
	 *                     wycinek od pocz¹tku szablonu
	 * @param keyToIndex   indeks klucza okreœlaj¹cego koniec wycinka szablonu,
	 *                     który ma zostaæ zwrócony bêd¹cy liczb¹ od 0 do n-1, gdzie
	 *                     n to liczba kluczy w szablonie lub -1 je¿eli ma byæ to
	 *                     wycinek do koñca szablonu
	 * @return wycinek szablonu pomiêdzy kluczami o indeksie <tt>keyIndexFrom</tt> i
	 *         <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je¿eli przynajmniej jeden z kluczy o
	 *                                      podanym indeksie nie istnieje
	 */
	public ITemplate cut(int keyFromIndex, int keyToIndex) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca sekwencjê szablonu pomiêdzy kluczem o nazwie <tt>keyNameFrom</tt>, a
	 * kluczem o nazwie <tt>keyNameTo</tt>. Parametr <tt>nLoops</tt> okreœla wstêpn¹
	 * liczbê pêtli jaka zostanie wykonana na zwracanej sekwencji.
	 * <p>
	 * Je¿eli parametr <tt>keyNameFrom</tt> bêdzie mia³ wartoœæ <tt>null</tt>
	 * wówczas bêdzie on traktowany jako pocz¹tek szablonu. Je¿eli zaœ parametr
	 * <tt>keyNameTo</tt> bêdzie mia³ wartoœæ <tt>null</tt> wówczas bêdzie on
	 * wskazywa³ na koniec szablonu. Przyk³adowo je¿eli chcielibyœmy pobraæ
	 * sekwencjê szablonu <tt>ITemplateSequence</tt> od jego pocz¹tku do klucza
	 * "KEY_A" wówczas wywo³ujemy metodê
	 * <tt>tamplate.newSequence(null, "KEY_A", nLoops);</tt>.
	 * 
	 * @param keyFromName   nazwa klucza okreœlaj¹cego pocz¹tek zwracanej sekwencji
	 *                      szablonu lub <tt>null</tt> je¿eli ma byæ to sekwencja od
	 *                      pocz¹tku szablonu
	 * @param keyToName     nazwa klucza okreœlaj¹cego koniec zwracanej sekwencji
	 *                      szablonu lub <tt>null</tt> je¿eli ma byæ to sekwencja do
	 *                      koñca szablonu
	 * @param innerKeyNames TODO
	 * @param nLoops        wstêpna liczba pêtli, któr¹ zostanie zainicjalizowana
	 *                      zwracana sekwencja
	 * @return sekwencja szablonu pomiêdzy kluczami o nazwie <tt>keyNameFrom</tt> i
	 *         <tt>keyNameTo</tt>
	 * @throws TemplateKeyNotFoundException je¿eli przynajmniej jeden z kluczy o
	 *                                      podanej nazwie nie istnieje
	 */
	public ITemplateSequence newSequence(String keyFromName, String keyToName, String[] innerKeyNames, int nLoops)
			throws TemplateKeyNotFoundException;

	/**
	 * Zwraca sekwencjê <tt>ITemplateSequence</tt> szablonu pomiêdzy kluczem o
	 * indeksie <tt>keyIndexFrom</tt>, a kluczem o indeksie <tt>keyIndexTo</tt>.
	 * Parametr <tt>nLoops</tt> okreœla wstêpn¹ liczbê pêtli jaka zostanie wykonana
	 * na zwracanej sekwencji.
	 * <p>
	 * Je¿eli parametr <tt>keyIndexFrom</tt> bêdzie mia³ wartoœæ -1 wówczas bêdzie
	 * on traktowany jako pocz¹tek szablonu. Je¿eli zaœ parametr <tt>keyIndexTo</tt>
	 * bêdzie mia³ wartoœæ -1 wówczas bêdzie on wskazywa³ na koniec szablonu.
	 * Przyk³adowo je¿eli chcielibyœmy pobraæ sekwencjê szablonu
	 * <tt>ITemplateSequence</tt> od jego pocz¹tku do klucza o indeksie 2 wówczas
	 * wywo³ujemy metodê <tt>tamplate.newSequence(-1, 2, nLoops);</tt>.
	 * 
	 * @param keyFromIndex  indeks klucza okreœlaj¹cego pocz¹tek zwracanej sekwencji
	 *                      szablonu bêd¹cy liczb¹ od 0 do n-1, gdzie n to liczba
	 *                      kluczy w szablonie lub -1 je¿eli ma byæ to sekwencja od
	 *                      pocz¹tku szablonu
	 * @param keyToIndex    indeks klucza okreœlaj¹cego koniec zwracanej sekwencji
	 *                      szablonu bêd¹cy liczb¹ od 0 do n-1, gdzie n to liczba
	 *                      kluczy w szablonie lub -1 je¿eli ma byæ to sekwencja do
	 *                      koñca szablonu
	 * @param innerKeyNames TODO
	 * @param nLoops        wstêpna liczba pêtli, któr¹ zostanie zainicjalizowana
	 *                      zwracana sekwencja
	 * @return sekwencja szablonu pomiêdzy kluczami o indeksie <tt>keyIndexFrom</tt>
	 *         i <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je¿eli przynajmniej jeden z kluczy o
	 *                                      podanym indeksie nie istnieje
	 */
	public ITemplateSequence newSequence(int keyFromIndex, int keyToIndex, String[] innerKeyNames, int nLoops)
			throws TemplateKeyNotFoundException;

	/**
	 * Zwraca pierwszy klucz o podanej nazwie lub <tt>null</tt> je¿eli klucz o
	 * takiej nazwie nie istnieje.
	 * 
	 * @param keyName nazwa szukanego klucza
	 * @return klucz o podanej nazwie lub <tt>null</tt>
	 */
	public ITemplateKey getKey(String keyName);

	/**
	 * 
	 * @param keyName
	 * @return
	 */
	public int getKeyIndex(String keyName);

	/**
	 * Zwraca klucz o danym indeksie
	 * 
	 * @param keyIndex nazwa szukanego klucza
	 * @return klucz o podanej nazwie lub <tt>null</tt>
	 */
	public ITemplateKey getKey(int keyIndex);

	/**
	 * Zwraca informacjê o tym czy klucz o podanej nazwie istnieje.
	 * 
	 * @param keyName nazwa szukanego klucza
	 * @return <tt>true</tt> je¿eli klucz o podanej nazwie istnieje, w przeciwnym
	 *         razie zwraca <tt>false</tt>
	 */
	public boolean hasKey(String keyName);

	/**
	 * Zwraca listê wszystkich dostêpnych kluczy w szablonie.
	 * 
	 * @return listê dostepnych kluczy
	 */
	public List<ITemplateKey> getKeys();

	/**
	 * Zwraca liczbê dostêpnych kluczy.
	 * 
	 * @return liczba kluczy
	 */
	public int getKeyCount();

	/**
	 * Zapisuje zawartoœæ tekstow¹ szablonu do obiektu <tt>write</tt>.
	 * 
	 * @see net.hottree.template.IOutput#write(net.hottree.template.IWriter)
	 */
	public void write(IWriter writer);

	/**
	 * Zwraca d³ugoœæ szablonu.
	 * 
	 * @return d³ugoœæ szablonu
	 */
	public int getLength();

	/**
	 * Zwraca tekst szablonu.
	 * 
	 * @return tekst szablonu
	 */
	public String toString();

	/**
	 * Zwraca klon szablonu.
	 * 
	 * @return klon szablonu
	 */
	public ITemplate clone();

}
