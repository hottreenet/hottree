package net.hottree.template;

import java.util.List;

import net.hottree.template.impl.TemplateKeyNotFoundException;

/**
 * Interfejs szablon�w w HotTree. Zawiera on metody i klucze do operacji na ich
 * tre�ci. Operacje nie s� zsynchronizowane.
 * <p>
 * <b>Note:</b> Metody w kt�rych zamiast nazwy podaje si� indeks klucza s�
 * szybsze od tych gdzie podaje si� nazw� klucza. Indeksy kluczy maj� warto�� od
 * 0 do n - 1, gdzie n to liczba kluczy. Ponadto indeksy kluczy odnosz� si� do
 * aktualnej liczby kluczy.
 */
public interface ITemplate extends IOutput, IDisposable {

	/**
	 * Zwraca wzgl�dn� �cie�k� do pliku szablonu.
	 * 
	 * @return wzgl�dna �cie�ka do pliku szablonu
	 */
	public String getLocation();

	/**
	 * Zwraca nazw� szablonu.
	 * 
	 * @return nazw� szablonu; je�eli nie b�dzie to wycinek szablonu to zwr�ci
	 *         wzgl�dn� �cie�k� do pliku szablonu
	 */
	public String getName();

	/**
	 * Dodaje do ko�ca szablonu obiekt podany przez parametr <tt>object</tt>. Je�eli
	 * nie jest on typu <tt>IOutput</tt> to zostanie on sprowadzony do tekstu przez
	 * metod� <tt>Object.toString()</tt>.
	 * 
	 * @param object obiekt, kt�ry ma by� dodany do ko�ca szablonu
	 */
	public void append(Object object);

	/**
	 * Zast�puje wszystkie klucze o podanej przez parametr <tt>keyName</tt> nazwie
	 * obiektem przekazanym przez parametr <tt>object</tt>. Je�eli podany obiekt nie
	 * jest typu <tt>IOutput</tt> to zostanie on sprowadzony do tekstu przez jego
	 * metod� <tt>Object.toString()</tt>.
	 * 
	 * @param keyName nazwa klucza(y), kt�ry ma zosta� zast�pionym obiektem
	 * @param object  obiekt, kt�ry ma by� wstawiony w miejsce klucza(y)
	 * @throws TemplateKeyNotFoundException je�eli klucz o podanej nazwie nie
	 *                                      istnieje
	 */
	public void replace(String keyName, Object object) throws TemplateKeyNotFoundException;

	/**
	 * Zast�puje klucz o podanym przez parametr <tt>keyIndex</tt> indeksie obiektem
	 * podanym przez parametr <tt>object</tt>. Je�eli podany obiekt nie jest typu
	 * <tt>IOutput</tt> to zostanie on sprowadzony do tekstu przez metod�
	 * <tt>Object.toString()</tt>.
	 * 
	 * @param keyIndex indeks klucza, kt�ry ma by� zast�piony b�d�cy liczb� od 0 do
	 *                 n-1, gdzie n to liczba kluczy w szablonie
	 * @param object   obiekt, kt�ry ma by� wstawiony w miejsce klucza
	 * @throws TemplateKeyNotFoundException je�eli klucz o podanym indeksie nie
	 *                                      istnieje
	 */
	public void replace(int keyIndex, Object object) throws TemplateKeyNotFoundException;

	/**
	 * Zast�puje wszystkie fragmenty szablonu zawarte pomi�dzy kluczam o nazwie
	 * <tt>keyNameFrom</tt>, a kluczam o nazwie <tt>keyNameTo</tt> obiektem podanym
	 * przez parametr <tt>object</tt>. Je�eli podany obiekt nie jest typu
	 * <tt>IOutput</tt> to zostanie on sprowadzony do tekstu przez metod�
	 * <tt>Object.toString()</tt>.
	 * <p>
	 * Je�eli parametr <tt>keyNameFrom</tt> b�dzie mia� warto�� <tt>null</tt>
	 * w�wczas b�dzie on traktowany jako pocz�tek szablonu. Je�eli za� parametr
	 * <tt>keyNameTo</tt> b�dzie mia� warto�� <tt>null</tt> w�wczas b�dzie on
	 * wskazywa� na koniec szablonu. Przyk�adowo je�eli chcieliby�my zast�pi�
	 * szablon od jego pocz�tku do klucza "KEY_A" (��cznie) pustym tekstem w�wczas
	 * wywo�ujemy metod� <tt>tamplate.replace(null, "KEY_A", "");</tt>.
	 * <p>
	 * <b>Note:</b> Fragment szablonu jest zast�powany ��cznie ze wskazanymi
	 * kluczami granicznymi.
	 * 
	 * @param keyFromName nazwa klucza okre�laj�cego pocz�tek fragmentu szablonu,
	 *                    kt�ry ma zosta� zast�piony lub <tt>null</tt> je�eli
	 *                    szablon ma by� zast�piony od jego pocz�tku
	 * @param keyToName   nazwa klucza okre�laj�cego koniec fragmentu szablonu,
	 *                    kt�ry ma zosta� zast�piony lub <tt>null</tt> je�eli
	 *                    szablon ma by� zast�piony do jego ko�ca
	 * @param object      obiekt, kt�ry ma by� wstawiony w miejsce szablonu
	 *                    okre�lonego przez parametry <tt>keyNameFrom</tt> i
	 *                    <tt>keyNameTo</tt>
	 * @throws TemplateKeyNotFoundException je�eli przynajmniej jeden z kluczy o
	 *                                      podanej nazwie nie istnieje
	 */
	public void replace(String keyFromName, String keyToName, Object object) throws TemplateKeyNotFoundException;

	/**
	 * Zast�puje fragment szablonu pomi�dzy kluczem o indeksie
	 * <tt>keyIndexFrom</tt>, a kluczem o indeksie <tt>keyIndexTo</tt> obiektem
	 * podanym przez paramtr <tt>object</tt>. Je�eli podany obiekt nie jest typu
	 * <tt>IOutput</tt> to zostanie on sprowadzony do tekstu przez metod�
	 * <tt>Object.toString()</tt>.
	 * <p>
	 * Je�eli parametr <tt>keyIndexFrom</tt> b�dzie mia� warto�� -1 w�wczas b�dzie
	 * on traktowany jako pocz�tek szablonu. Je�eli za� parametr <tt>keyIndexTo</tt>
	 * b�dzie mia� warto�� -1 w�wczas b�dzie on wskazywa� na koniec szablonu.
	 * Przyk�adowo je�eli chcieliby�my zast�pi� szablon od jego pocz�tku do klucza o
	 * indeksie 2 (��cznie) pustym tekstem w�wczas wywo�ujemy metod�
	 * <tt>tamplate.replace(-1, 2, "");</tt>.
	 * <p>
	 * <b>Note:</b> Fragment szablonu jest zast�powany ��cznie ze wskazanymi
	 * kluczami granicznymi. Indeksy kluczy s� naliczane od 0 do n - 1; gdzie n to
	 * liczba dost�pnych kluczy w szablonie.
	 * 
	 * @param keyFromIndex indeks klucza okre�laj�cego pocz�tek fragmentu szablonu,
	 *                     kt�ry ma zosta� zast�piony b�d�cy liczb� od 0 do n-1,
	 *                     gdzie n to liczba kluczy w szablonie, lub -1 je�eli
	 *                     szablon ma by� zast�piony od jego pocz�tku
	 * @param keyToIndex   indeks klucza okre�laj�cego koniec fragmentu szablonu,
	 *                     kt�ry ma zosta� zast�piony b�d�cy liczb� od 0 do n-1,
	 *                     gdzie n to liczba kluczy w szablonie lub -1 je�eli
	 *                     szablon ma by� zast�piony do jego ko�ca
	 * @param object       obiekt, kt�ry ma by� wstawiony w miejsce szablonu
	 *                     okre�lonego przez parametry <tt>keyIndexFrom</tt> i
	 *                     <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je�eli przynajmniej jeden z kluczy o
	 *                                      podanym indeksie nie istnieje
	 */
	public void replace(int keyFromIndex, int keyToIndex, Object object) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca szablon b�d�cy kopi� szablonu pomi�dzy kluczem o nazwie
	 * <tt>keyNameFrom</tt> , a kluczem o nazwie <tt>keyNameTo</tt>.
	 * <p>
	 * Je�eli parametr <tt>keyNameFrom</tt> b�dzie mia� warto�� <tt>null</tt>
	 * w�wczas b�dzie on traktowany jako pocz�tek szablonu. Je�eli za� parametr
	 * <tt>keyNameTo</tt> b�dzie mia� warto�� <tt>null</tt> w�wczas b�dzie on
	 * wskazywa� na koniec szablonu. Przyk�adowo je�eli chcieliby�my skopiowa�
	 * szablon od jego pocz�tku do klucza "KEY_A" w�wczas wywo�ujemy metod�
	 * <tt>tamplate.copy(null, "KEY_A");</tt>.
	 * 
	 * @param keyFromName nazwa klucza okre�laj�cego pocz�tek kopi szablonu, kt�ra
	 *                    ma zosta� zwr�cona lub <tt>null</tt> je�eli ma by� to
	 *                    kopia od pocz�tku szablonu
	 * @param keyToName   nazwa klucza okre�laj�cego koniec kopi szablonu, kt�ra ma
	 *                    zosta� zwr�cona lub <tt>null</tt> je�eli ma by� to kopia
	 *                    do ko�ca szablonu
	 * @return kopia szablonu okre�lona przez parametry <tt>keyIndexFrom</tt> i
	 *         <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je�eli przynajmniej jeden z kluczy o
	 *                                      podanej nazwie nie istnieje
	 */
	public ITemplate copy(String keyFromName, String keyToName) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca szablon b�d�cy kopi� szablonu pomi�dzy kluczem o indeksie
	 * <tt>keyIndexFrom</tt>, a kluczem o indeksie <tt>keyIndexTo</tt>.
	 * <p>
	 * Je�eli parametr <tt>keyIndexFrom</tt> b�dzie mia� warto�� -1 w�wczas b�dzie
	 * on traktowany jako pocz�tek szablonu. Je�eli za� parametr <tt>keyIndexTo</tt>
	 * b�dzie mia� warto�� -1 w�wczas b�dzie on wskazywa� na koniec szablonu.
	 * Przyk�adowo je�eli chcieliby�my skopiowa� szablon od jego pocz�tku do klucza
	 * o indeksie 2 w�wczas wywo�ujemy metod� <tt>tamplate.copy(-1, 2);</tt>.
	 * <p>
	 * <b>Note:</b> Indeksy kluczy s� naliczane od 0 do n - 1; gdzie n to liczba
	 * dost�pnych kluczy w szablonie.
	 * 
	 * @param keyFromIndex indeks klucza okre�laj�cego pocz�tek kopi szablonu, kt�ra
	 *                     ma zosta� zwr�cona b�d�cy liczb� od 0 do n-1, gdzie n to
	 *                     liczba kluczy w szablonie lub -1 je�eli ma by� to kopia
	 *                     od pocz�tku szablonu
	 * @param keyToIndex   indeks klucza okre�laj�cego koniec kopi szablonu, kt�ra
	 *                     ma zosta� zwr�cona b�d�cy liczb� od 0 do n-1, gdzie n to
	 *                     liczba kluczy w szablonie lub -1 je�eli ma by� to kopia
	 *                     do ko�ca szablonu
	 * @return kopia szablonu okre�lona przez parametry <tt>keyIndexFrom</tt> i
	 *         <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je�eli przynajmniej jeden z kluczy o
	 *                                      podanym indeksie nie istnieje
	 */
	public ITemplate copy(int keyFromIndex, int keyToIndex) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca tekst szablonu pomi�dzy kluczem o nazwie <tt>keyNameFrom</tt>, a
	 * kluczem o nazwie <tt>keyNameTo</tt>.
	 * <p>
	 * Je�eli parametr <tt>keyNameFrom</tt> b�dzie mia� warto�� <tt>null</tt>
	 * w�wczas b�dzie on traktowany jako pocz�tek szablonu. Je�eli za� parametr
	 * <tt>keyNameTo</tt> b�dzie mia� warto�� <tt>null</tt> w�wczas b�dzie on
	 * wskazywa� na koniec szablonu. Przyk�adowo je�eli chcieliby�my zwr�ci� tekst
	 * od pocz�tku szablonu do klucza "KEY_A" w�wczas wywo�ujemy metod�
	 * <tt>tamplate.copyText(null, "KEY_A");</tt>.
	 * 
	 * @param keyFromName nazwa klucza okre�laj�cego pocz�tek tekstu, kt�ry ma
	 *                    zosta� zwr�cony lub <tt>null</tt> je�eli ma by� to tekst
	 *                    od pocz�tku szablonu
	 * @param keyToName   nazwa klucza okre�laj�cego koniec tekstu, kt�ry ma zosta�
	 *                    zwr�cony lub <tt>null</tt> je�eli ma by� to tekst do ko�ca
	 *                    szablonu
	 * @return tekst szablonu pomi�dzy kluczami o nazwie <tt>keyNameFrom</tt>, i
	 *         <tt>keyNameTo</tt>
	 * @throws TemplateKeyNotFoundException je�eli przynajmniej jeden z kluczy o
	 *                                      podanej nazwie nie istnieje
	 */
	public String copyText(String keyFromName, String keyToName) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca tekst szablon pomi�dzy kluczem o indeksie <tt>keyIndexFrom</tt>, a
	 * kluczem o indeksie <tt>keyIndexTo</tt>.
	 * <p>
	 * Je�eli parametr <tt>keyIndexFrom</tt> b�dzie mia� warto�� -1 w�wczas b�dzie
	 * on traktowany jako pocz�tek szablonu. Je�eli za� parametr <tt>keyIndexTo</tt>
	 * b�dzie mia� warto�� -1 w�wczas b�dzie on wskazywa� na koniec szablonu.
	 * Przyk�adowo je�eli chcieliby�my zwr�ci� tekst od pocz�tku szablonu do klucza
	 * o indeksie 2 w�wczas wywo�ujemy metod� <tt>tamplate.copyText(-1, 2);</tt>.
	 * <p>
	 * <b>Note:</b> Indeksy kluczy s� naliczane od 0 do n - 1; gdzie n to liczba
	 * dost�pnych kluczy w szablonie.
	 * 
	 * @param keyFromIndex indeks klucza okre�laj�cego pocz�tek tekstu, kt�ry ma
	 *                     zosta� zwr�cony b�d�cy liczb� od 0 do n-1, gdzie n to
	 *                     liczba kluczy w szablonie lub -1 je�eli ma by� to tekst
	 *                     od pocz�tku szablonu
	 * @param keyToIndex   indeks klucza okre�laj�cego koniec tekstu, kt�ry ma
	 *                     zosta� zwr�cony b�d�cy liczb� od 0 do n-1, gdzie n to
	 *                     liczba kluczy w szablonie lub -1 je�eli ma by� to tekst
	 *                     do ko�ca szablonu
	 * @return tekst szablonu pomi�dzy kluczami o indeksie <tt>keyIndexFrom</tt> , i
	 *         <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je�eli przynajmniej jeden z kluczy o
	 *                                      podanym indeksie nie istnieje
	 */
	public String copyText(int keyFromIndex, int keyToIndex) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca wycinek szablonu pomi�dzy kluczem o nazwie <tt>keyNameFrom</tt>, a
	 * kluczem o nazwie <tt>keyNameTo</tt>.
	 * <p>
	 * Je�eli parametr <tt>keyNameFrom</tt> b�dzie mia� warto�� <tt>null</tt>
	 * w�wczas b�dzie on traktowany jako pocz�tek szablonu. Je�eli za� parametr
	 * <tt>keyNameTo</tt> b�dzie mia� warto�� <tt>null</tt> w�wczas b�dzie on
	 * wskazywa� na koniec szablonu. Przyk�adowo je�eli chcieliby�my zwr�ci� wycinek
	 * szablonu od jego pocz�tku do klucza "KEY_A" w�wczas wywo�ujemy metod�
	 * <tt>tamplate.cut(null, "KEY_A");</tt>.
	 * 
	 * 
	 * @param keyFromName nazwa klucza okre�laj�cego pocz�tek wycinka szablonu,
	 *                    kt�ry ma zosta� zwr�cony lub <tt>null</tt> je�eli ma by�
	 *                    to wycinek od pocz�tku szablonu
	 * @param keyToName   nazwa klucza okre�laj�cego koniec wycinka szablonu, kt�ry
	 *                    ma zosta� zwr�cony lub <tt>null</tt> je�eli ma by� to
	 *                    wycinek do ko�ca szablonu
	 * @return wycinek szablonu pomi�dzy kluczami o nazwie <tt>keyNameFrom</tt> i
	 *         <tt>keyNameTo</tt>
	 * @throws TemplateKeyNotFoundException je�eli przynajmniej jeden z kluczy o
	 *                                      podanej nazwie nie istnieje
	 */
	public ITemplate cut(String keyFromName, String keyToName) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca wycinek szablonu pomi�dzy kluczem o indeksie <tt>keyIndexFrom</tt> , a
	 * kluczem o indeksie <tt>keyIndexTo</tt>.
	 * <p>
	 * Je�eli parametr <tt>keyIndexFrom</tt> b�dzie mia� warto�� -1 w�wczas b�dzie
	 * on traktowany jako pocz�tek szablonu. Je�eli za� parametr <tt>keyIndexTo</tt>
	 * b�dzie mia� warto�� -1 w�wczas b�dzie on wskazywa� na koniec szablonu.
	 * Przyk�adowo je�eli chcieliby�my zwr�ci� wycinek szablonu od jego pocz�tku do
	 * klucza o indeksie 2 w�wczas wywo�ujemy metod� <tt>tamplate.cut(-1, 2);</tt>.
	 * <p>
	 * <b>Note:</b> Indeksy kluczy s� naliczane od 0 do n - 1; gdzie n to liczba
	 * dost�pnych kluczy w szablonie.
	 * 
	 * @param keyFromIndex indeks klucza okre�laj�cego pocz�tek wycinka szablonu,
	 *                     kt�ry ma zosta� zwr�cony b�d�cy liczb� od 0 do n-1, gdzie
	 *                     n to liczba kluczy w szablonie lub -1 je�eli ma by� to
	 *                     wycinek od pocz�tku szablonu
	 * @param keyToIndex   indeks klucza okre�laj�cego koniec wycinka szablonu,
	 *                     kt�ry ma zosta� zwr�cony b�d�cy liczb� od 0 do n-1, gdzie
	 *                     n to liczba kluczy w szablonie lub -1 je�eli ma by� to
	 *                     wycinek do ko�ca szablonu
	 * @return wycinek szablonu pomi�dzy kluczami o indeksie <tt>keyIndexFrom</tt> i
	 *         <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je�eli przynajmniej jeden z kluczy o
	 *                                      podanym indeksie nie istnieje
	 */
	public ITemplate cut(int keyFromIndex, int keyToIndex) throws TemplateKeyNotFoundException;

	/**
	 * Zwraca sekwencj� szablonu pomi�dzy kluczem o nazwie <tt>keyNameFrom</tt>, a
	 * kluczem o nazwie <tt>keyNameTo</tt>. Parametr <tt>nLoops</tt> okre�la wst�pn�
	 * liczb� p�tli jaka zostanie wykonana na zwracanej sekwencji.
	 * <p>
	 * Je�eli parametr <tt>keyNameFrom</tt> b�dzie mia� warto�� <tt>null</tt>
	 * w�wczas b�dzie on traktowany jako pocz�tek szablonu. Je�eli za� parametr
	 * <tt>keyNameTo</tt> b�dzie mia� warto�� <tt>null</tt> w�wczas b�dzie on
	 * wskazywa� na koniec szablonu. Przyk�adowo je�eli chcieliby�my pobra�
	 * sekwencj� szablonu <tt>ITemplateSequence</tt> od jego pocz�tku do klucza
	 * "KEY_A" w�wczas wywo�ujemy metod�
	 * <tt>tamplate.newSequence(null, "KEY_A", nLoops);</tt>.
	 * 
	 * @param keyFromName   nazwa klucza okre�laj�cego pocz�tek zwracanej sekwencji
	 *                      szablonu lub <tt>null</tt> je�eli ma by� to sekwencja od
	 *                      pocz�tku szablonu
	 * @param keyToName     nazwa klucza okre�laj�cego koniec zwracanej sekwencji
	 *                      szablonu lub <tt>null</tt> je�eli ma by� to sekwencja do
	 *                      ko�ca szablonu
	 * @param innerKeyNames TODO
	 * @param nLoops        wst�pna liczba p�tli, kt�r� zostanie zainicjalizowana
	 *                      zwracana sekwencja
	 * @return sekwencja szablonu pomi�dzy kluczami o nazwie <tt>keyNameFrom</tt> i
	 *         <tt>keyNameTo</tt>
	 * @throws TemplateKeyNotFoundException je�eli przynajmniej jeden z kluczy o
	 *                                      podanej nazwie nie istnieje
	 */
	public ITemplateSequence newSequence(String keyFromName, String keyToName, String[] innerKeyNames, int nLoops)
			throws TemplateKeyNotFoundException;

	/**
	 * Zwraca sekwencj� <tt>ITemplateSequence</tt> szablonu pomi�dzy kluczem o
	 * indeksie <tt>keyIndexFrom</tt>, a kluczem o indeksie <tt>keyIndexTo</tt>.
	 * Parametr <tt>nLoops</tt> okre�la wst�pn� liczb� p�tli jaka zostanie wykonana
	 * na zwracanej sekwencji.
	 * <p>
	 * Je�eli parametr <tt>keyIndexFrom</tt> b�dzie mia� warto�� -1 w�wczas b�dzie
	 * on traktowany jako pocz�tek szablonu. Je�eli za� parametr <tt>keyIndexTo</tt>
	 * b�dzie mia� warto�� -1 w�wczas b�dzie on wskazywa� na koniec szablonu.
	 * Przyk�adowo je�eli chcieliby�my pobra� sekwencj� szablonu
	 * <tt>ITemplateSequence</tt> od jego pocz�tku do klucza o indeksie 2 w�wczas
	 * wywo�ujemy metod� <tt>tamplate.newSequence(-1, 2, nLoops);</tt>.
	 * 
	 * @param keyFromIndex  indeks klucza okre�laj�cego pocz�tek zwracanej sekwencji
	 *                      szablonu b�d�cy liczb� od 0 do n-1, gdzie n to liczba
	 *                      kluczy w szablonie lub -1 je�eli ma by� to sekwencja od
	 *                      pocz�tku szablonu
	 * @param keyToIndex    indeks klucza okre�laj�cego koniec zwracanej sekwencji
	 *                      szablonu b�d�cy liczb� od 0 do n-1, gdzie n to liczba
	 *                      kluczy w szablonie lub -1 je�eli ma by� to sekwencja do
	 *                      ko�ca szablonu
	 * @param innerKeyNames TODO
	 * @param nLoops        wst�pna liczba p�tli, kt�r� zostanie zainicjalizowana
	 *                      zwracana sekwencja
	 * @return sekwencja szablonu pomi�dzy kluczami o indeksie <tt>keyIndexFrom</tt>
	 *         i <tt>keyIndexTo</tt>
	 * @throws TemplateKeyNotFoundException je�eli przynajmniej jeden z kluczy o
	 *                                      podanym indeksie nie istnieje
	 */
	public ITemplateSequence newSequence(int keyFromIndex, int keyToIndex, String[] innerKeyNames, int nLoops)
			throws TemplateKeyNotFoundException;

	/**
	 * Zwraca pierwszy klucz o podanej nazwie lub <tt>null</tt> je�eli klucz o
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
	 * Zwraca informacj� o tym czy klucz o podanej nazwie istnieje.
	 * 
	 * @param keyName nazwa szukanego klucza
	 * @return <tt>true</tt> je�eli klucz o podanej nazwie istnieje, w przeciwnym
	 *         razie zwraca <tt>false</tt>
	 */
	public boolean hasKey(String keyName);

	/**
	 * Zwraca list� wszystkich dost�pnych kluczy w szablonie.
	 * 
	 * @return list� dostepnych kluczy
	 */
	public List<ITemplateKey> getKeys();

	/**
	 * Zwraca liczb� dost�pnych kluczy.
	 * 
	 * @return liczba kluczy
	 */
	public int getKeyCount();

	/**
	 * Zapisuje zawarto�� tekstow� szablonu do obiektu <tt>write</tt>.
	 * 
	 * @see net.hottree.template.IOutput#write(net.hottree.template.IWriter)
	 */
	public void write(IWriter writer);

	/**
	 * Zwraca d�ugo�� szablonu.
	 * 
	 * @return d�ugo�� szablonu
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
