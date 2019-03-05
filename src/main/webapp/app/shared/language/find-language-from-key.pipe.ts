import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'findLanguageFromKey' })
export class FindLanguageFromKeyPipe implements PipeTransform {
    private languages: any = {
        'zh-cn': { name: '中文（简体）' },
        cs: { name: 'Český' },
        en: { name: 'English' },
        et: { name: 'Eesti' },
        fr: { name: 'Français' },
        gl: { name: 'Galego' },
        de: { name: 'Deutsch' },
        el: { name: 'Ελληνικά' },
        ko: { name: '한국어' },
        pl: { name: 'Polski' },
        'pt-pt': { name: 'Português' },
        ru: { name: 'Русский' },
        es: { name: 'Español' },
        sv: { name: 'Svenska' },
        tr: { name: 'Türkçe' }
        // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
    };
    transform(lang: string): string {
        return this.languages[lang].name;
    }
}
